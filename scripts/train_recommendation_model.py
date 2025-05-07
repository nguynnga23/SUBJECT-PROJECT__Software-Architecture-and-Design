import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sqlalchemy import create_engine
import json
import logging
import os

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 1. Load and preprocess data
def load_data():
    # Connect to PostgreSQL
    engine = create_engine('postgresql://postgre:root@localhost:5432/borrowing-service')
    query = """
    SELECT rr.reader_id AS user_id, rrd.book_copy_id
    FROM reader_requests rr
    JOIN reader_request_details rrd ON rr.id = rrd.reader_request_id
    WHERE rr.status = 'APPROVED' AND rr.date_borrowed IS NOT NULL
    """
    logger.info("Executing SQL query")
    df = pd.read_sql(query, engine)
    logger.info(f"Raw data shape: {df.shape}")
    logger.info(f"Raw data:\n{df}")

    # Convert UUIDs to strings
    df['user_id'] = df['user_id'].astype(str)
    df['book_copy_id'] = df['book_copy_id'].astype(str)
    logger.info(f"Data types after conversion:\n{df.dtypes}")
    logger.info(f"Sample user_id: {df['user_id'].iloc[0]}, type: {type(df['user_id'].iloc[0])}")
    logger.info(f"Sample book_copy_id: {df['book_copy_id'].iloc[0]}, type: {type(df['book_copy_id'].iloc[0])}")

    # Check for missing values
    if df['user_id'].isnull().any() or df['book_copy_id'].isnull().any():
        logger.warning("Missing values found in user_id or book_copy_id")
        df = df.dropna(subset=['user_id', 'book_copy_id'])
        logger.info(f"Data shape after dropping NA: {df.shape}")

    # Create user-item interaction matrix
    users = df['user_id'].unique()
    books = df['book_copy_id'].unique()
    user2idx = {user: idx for idx, user in enumerate(users)}  # String keys
    book2idx = {book: idx for idx, book in enumerate(books)}  # String keys
    logger.info(f"Number of unique users: {len(users)}")
    logger.info(f"Number of unique book copies: {len(books)}")
    logger.info(f"Sample user2idx key: {list(user2idx.keys())[0]}, type: {type(list(user2idx.keys())[0])}")
    logger.info(f"Sample book2idx key: {list(book2idx.keys())[0]}, type: {type(list(book2idx.keys())[0])}")

    # Map IDs to indices
    df['user_idx'] = df['user_id'].map(user2idx)
    df['book_idx'] = df['book_copy_id'].map(book2idx)

    # Check for mapping issues
    if df['user_idx'].isnull().any() or df['book_idx'].isnull().any():
        logger.error("Mapping failed: NaN values in user_idx or book_idx")
        logger.error(f"NaN in user_idx: {df[df['user_idx'].isnull()][['user_id', 'book_copy_id']]}")
        logger.error(f"NaN in book_idx: {df[df['book_idx'].isnull()][['user_id', 'book_copy_id']]}")
        raise ValueError("Mapping failed: Check user_id and book_copy_id mappings")

    # Create implicit feedback
    interactions = df[['user_idx', 'book_idx']].copy()
    interactions['rating'] = 1  # Implicit feedback
    logger.info(f"Interactions shape: {interactions.shape}")
    logger.info(f"Interactions:\n{interactions}")

    # Add negative samples
    num_negatives = len(interactions) * 2  # 2x positive samples
    negative_samples = []
    existing_pairs = set(zip(interactions['user_idx'], interactions['book_idx']))
    while len(negative_samples) < num_negatives:
        user_idx = np.random.choice(interactions['user_idx'])
        book_idx = np.random.choice(interactions['book_idx'])
        if (user_idx, book_idx) not in existing_pairs:
            negative_samples.append([user_idx, book_idx, 0])
    negative_df = pd.DataFrame(negative_samples, columns=['user_idx', 'book_idx', 'rating'])
    interactions = pd.concat([interactions, negative_df], ignore_index=True)
    logger.info(f"Interactions with negatives shape: {interactions.shape}")
    logger.info(f"Negative samples added: {len(negative_df)}")

    # Validate data size
    if len(interactions) < 5:
        logger.error(f"Insufficient data: Only {len(interactions)} interactions")
        raise ValueError("Need at least 5 interactions for training")

    return interactions, len(users), len(books), user2idx, book2idx

# 2. Build neural collaborative filtering model
def build_model(num_users, num_books, embedding_dim=50):
    user_input = tf.keras.layers.Input(shape=(1,), name='user_input', dtype=tf.int32)
    book_input = tf.keras.layers.Input(shape=(1,), name='book_input', dtype=tf.int32)

    # User and book embeddings
    user_embedding = tf.keras.layers.Embedding(
        num_users, embedding_dim, name='user_embedding',
        embeddings_regularizer=tf.keras.regularizers.l2(1e-6)
    )(user_input)
    book_embedding = tf.keras.layers.Embedding(
        num_books, embedding_dim, name='book_embedding',
        embeddings_regularizer=tf.keras.regularizers.l2(1e-6)
    )(book_input)

    # Flatten embeddings
    user_vec = tf.keras.layers.Flatten(name='flatten_user')(user_embedding)
    book_vec = tf.keras.layers.Flatten(name='flatten_book')(book_embedding)

    # Dot product and neural layers
    dot_product = tf.keras.layers.Dot(axes=1, name='dot_product')([user_vec, book_vec])
    x = tf.keras.layers.Dense(128, activation='relu', name='dense_1')(dot_product)
    x = tf.keras.layers.Dense(64, activation='relu', name='dense_2')(x)
    output = tf.keras.layers.Dense(1, activation='sigmoid', name='output')(x)

    model = tf.keras.Model(inputs=[user_input, book_input], outputs=output)
    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
    return model

# 3. Train and export model
def train_model():
    interactions, num_users, num_books, user2idx, book2idx = load_data()

    # Prepare training data
    user_idx = interactions['user_idx'].values.copy().astype(np.int32).reshape(-1, 1)
    book_idx = interactions['book_idx'].values.copy().astype(np.int32).reshape(-1, 1)
    y = interactions['rating'].values.copy().astype(np.float32)

    # Validate array shapes and dtypes
    logger.info(f"X[0] (user_idx) shape: {user_idx.shape}, dtype: {user_idx.dtype}, values: {user_idx.flatten()}")
    logger.info(f"X[1] (book_idx) shape: {book_idx.shape}, dtype: {book_idx.dtype}, values: {book_idx.flatten()}")
    logger.info(f"y (rating) shape: {y.shape}, dtype: {y.dtype}, values: {y}")
    if not (user_idx.shape[0] == book_idx.shape[0] == y.shape[0]):
        logger.error(f"Inconsistent array lengths: user_idx={user_idx.shape[0]}, book_idx={book_idx.shape[0]}, rating={y.shape[0]}")
        raise ValueError("Inconsistent array lengths in X and y")

    # Combine user_idx and book_idx into a single 2D array
    X_combined = np.hstack([user_idx, book_idx])
    logger.info(f"X_combined shape: {X_combined.shape}, values:\n{X_combined}")

    # Split combined array
    X_train, X_test, y_train, y_test = train_test_split(X_combined, y, test_size=0.2, random_state=42)

    # Reconstruct list format for model
    X_train = [X_train[:, 0:1], X_train[:, 1:2]]
    X_test = [X_test[:, 0:1], X_test[:, 1:2]]
    logger.info(f"Training data: X_train[0]={X_train[0].shape}, X_test[0]={X_test[0].shape}")

    # Build and train model
    model = build_model(num_users, num_books)
    model.fit(
        X_train, y_train,
        epochs=10,
        batch_size=64,
        validation_data=(X_test, y_test),
        verbose=1
    )

    # Define serving signature
    @tf.function(input_signature=[
        tf.TensorSpec(shape=[None, 1], dtype=tf.int32, name='user_input'),
        tf.TensorSpec(shape=[None, 1], dtype=tf.int32, name='book_input')
    ])
    def serve(user_input, book_input):
        prediction = model([user_input, book_input], training=False)
        return {'output': prediction}

    # Export as SavedModel
    export_path = './saved_model'
    tf.saved_model.save(model, export_path, signatures={'serving_default': serve})

    # Create folder for mappings if it doesn't exist
    mappings_dir = './mappings'
    os.makedirs(mappings_dir, exist_ok=True)

    # Save index mappings
    with open(os.path.join(mappings_dir, 'user2idx.json'), 'w') as f:
        json.dump(user2idx, f)
    with open(os.path.join(mappings_dir, 'book2idx.json'), 'w') as f:
        json.dump(book2idx, f)

    logger.info(f"Model saved to {export_path}")
    logger.info(f"Index mappings saved to {mappings_dir}/user2idx.json and {mappings_dir}/book2idx.json")

# 4. Test recommendations
def test_recommendations():
    interactions, num_users, num_books, user2idx, book2idx = load_data()
    model = tf.saved_model.load('./saved_model')
    infer = model.signatures['serving_default']

    user_id = str(list(user2idx.keys())[0])  # Example UUID
    user_idx = user2idx[user_id]
    book_indices = np.arange(num_books)
    user_array = np.array([user_idx] * num_books, dtype=np.int32).reshape(-1, 1)
    book_array = book_indices.reshape(-1, 1).astype(np.int32)

    predictions = infer(
        user_input=tf.constant(user_array),
        book_input=tf.constant(book_array)
    )['output'].numpy()

    top_k = 5
    top_indices = np.argsort(predictions.flatten())[::-1][:top_k]
    recommended_book_ids = [list(book2idx.keys())[list(book2idx.values()).index(i)] for i in top_indices]
    logger.info(f"Recommended book copy IDs for user {user_id}: {recommended_book_ids}")

if __name__ == '__main__':
    train_model()
    test_recommendations()