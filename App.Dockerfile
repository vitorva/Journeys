FROM node:16.13.0-alpine
ENV NODE_OPTIONS=--max-old-space-size=8192
WORKDIR /app

COPY ./frontend ./
RUN npm install -s -g serve
RUN npm run build
EXPOSE 3000
