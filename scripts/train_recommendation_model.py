import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split
from sqlalchemy import create_engine

# 1. Load and preprocess data
def load_data():
    # Connect to PostgreSQL (from Borrowing Service)
    engine = create_engine('postgresql://postgres:password@localhost:5432/library')
    query = "SELECT user_id, book_id FROM borrowings"
    df = pd.read_sql(query, engine)

    # Create user-item interaction matrix
    users = df['user_id'].unique()
    books = df['book_id'].unique()
    user2idx = {user: idx for idx, user in enumerate(users)}
    book2idx = {book: idx for idx, book in enumerate(books)}

    # Map IDs to indices
    df['user_idx'] = df['user_id'].map(user2idx)
    df['book_idx'] = df['book_id'].map(book2idx)

    # Create implicit feedback (1 for borrow, 0 for no borrow)
    interactions = df[['user_idx', 'book_idx']].copy()
    interactions['rating'] = 1  # Implicit feedback

    return interactions, len(users), len(books), user2idx, book2idx

# 2. Build neural collaborative filtering model
def build_model(num_users, num_books, embedding_dim=50):
    user_input = tf.keras.layers.Input(shape=(1,), name='user_input')
    book_input = tf.keras.layers.Input(shape=(1,), name='book_input')

    # User and book embeddings
    user_embedding = tf.keras.layers.Embedding(num_users, embedding_dim, name='user_embedding')(user_input)
    book_embedding = tf.keras.layers.Embedding(num_books, embedding_dim, name='book_embedding')(book_input)

    # Flatten embeddings
    user_vec = tf.keras.layers.Flatten()(user_embedding)
    book_vec = tf.keras.layers.Flatten()(book_embedding)

    # Dot product of embeddings
    dot_product = tf.keras.layers.Dot(axes=1)([user_vec, book_vec])

    # Dense layers for neural CF
    x = tf.keras.layers.Dense(128, activation='relu')(dot_product)
    x = tf.keras.layers.Dense(64, activation='relu')(x)
    output = tf.keras.layers.Dense(1, activation='sigmoid', name='output')(x)

    model = tf.keras.Model(inputs=[user_input, book_input], outputs=output)
    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
    return model

# 3. Train model
def train_model():
    interactions, num_users, num_books, user2idx, book2idx = load_data()

    # Prepare training data
    X = [interactions['user_idx'].values, interactions['book_idx'].values]
    y = interactions['rating'].values

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    # Build and train model
    model = build_model(num_users, num_books)
    model.fit(X_train, y_train, epochs=10, batch_size=64, validation_data=(X_test, y_test))

    # Save model
    model.save('recommendation_model')
    print("Model saved to 'recommendation_model'")

# 4. Generate recommendations (for testing)
def get_recommendations(model, user_id, user2idx, book2idx, num_books, top_k=5):
    user_idx = user2idx[user_id]
    book_indices = np.arange(num_books)
    user_array = np.array([user_idx] * num_books)
    book_array = book_indices

    predictions = model.predict([user_array, book_array])
    top_indices = np.argsort(predictions.flatten())[::-1][:top_k]
    recommended_book_ids = [list(book2idx.keys())[list(book2idx.values()).index(i)] for i in top_indices]
    return recommended_book_ids

if __name__ == '__main__':
    train_model()