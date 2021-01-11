import dotenv from "dotenv";
import app from "./app";
import firebase from "./firebase";

dotenv.config();

console.log(firebase);

const PORT = process.env.PORT1;

const handleListening = () => {
  console.log(`Listening on : http://localhost:${PORT}`);
};

app.listen(PORT, handleListening);
