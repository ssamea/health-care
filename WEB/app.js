import express from "express";
import morgan from "morgan";
import helmet from "helmet";
import bodyParser from "body-parser";
import cookieParser from "cookie-parser";
import boardRouter from "./routers/boardRouter";
import userRouter from "./routers/userRouter";
import globalRouter from "./routers/globalRouter";
import router from "./router";

const app = express();

const handleHome = (req, res) => {
  res.send("It is my Home!");
};

app.use(cookieParser());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(helmet());
app.use(morgan("dev"));

app.get("/", handleHome);

app.use(router.home, globalRouter);
app.use(router.users, userRouter);
app.use(router.board, boardRouter);

export default app;
