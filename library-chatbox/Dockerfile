# Base image
FROM node:18

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the source code
COPY . .

# Expose port (sửa lại nếu bạn dùng port khác trong index.js)
EXPOSE 3000

# Start the application
CMD ["node", "index.js"]