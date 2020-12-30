const express = require("express");
const app = express();

const PORT1 = 806;

const handleListening = (req, res) => {
  console.log(`Listening on : http://localhost:${PORT1}`);
};

const handleHome = (req, res) => {
  res.send("It's home");
};

app.get("/", handleHome);

app.listen(PORT1, handleListening);

export default app;
