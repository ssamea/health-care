import express from "express";
import morgan from "morgan";
import helmet from "helmet";
import cookieParser from "cookie-parser";
import bodyParser from "body-parser";
import { localsMiddleWare } from "./middleware";
import router from "./router";
import boardRouter from "./routers/boardRouter";
import userRouter from "./routers/userRouter";
import globalRouter from "./routers/globalRouter";

const app = express();

app.use(
  helmet({
    contentSecurityPolicy: false,
  })
);

// Else
app.set("view engine", "pug");
app.use("/uploads", express.static("uploads"));
app.use("/static", express.static("static"));
app.use("/styles", express.static("styles"));
app.use("/image", express.static("image"));
app.use(cookieParser()); // 사용자 인증시 필요
app.use(bodyParser.json()); // 사용자가 웹 사이트로 전달하는 정보 검사
app.use(bodyParser.urlencoded({ extended: true }));
app.use(morgan("dev"));
app.use(localsMiddleWare);

app.use(router.home, globalRouter);
app.use(router.users, userRouter);
app.use(router.board, boardRouter);

export default app;
