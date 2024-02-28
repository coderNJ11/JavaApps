# Use an official Node.js runtime as the base image
FROM node:14

# Set the working directory within the container
WORKDIR /app

# Copy the package.json and package-lock.json files to the working directory
COPY package*.json ./

# Install the application dependencies
RUN npm install

# Copy the application source code to the working directory
COPY . .

# Expose the port on which the server will run
EXPOSE 3000

# Define the command to start the server
CMD [ "node", "--max-http-header-size=131872", "fromio" ]