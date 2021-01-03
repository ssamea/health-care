import express from "express";
import router from "../router";
import { home, join, login, logout } from "../controllers/globalController";

const globalRouter = express.Router();

globalRouter.get(router.home, home);
globalRouter.get(router.join, join);
globalRouter.get(router.login, login);
globalRouter.get(router.logout, logout);

export default globalRouter;
