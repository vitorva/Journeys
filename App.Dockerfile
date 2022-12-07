FROM node:16.13.0-alpine
WORKDIR /app

COPY ./frontend ./
RUN npm install -s -g serve
RUN npm run build
EXPOSE 3000
