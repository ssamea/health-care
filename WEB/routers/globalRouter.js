import express from "express";
import router from "../router";
import {
  home,
  getjoin,
  postjoin,
  getlogin,
  postlogin,
  logout,
  search,
  loginhome,
} from "../controllers/globalController";

const globalRouter = express.Router();

globalRouter.get(router.home, home);
globalRouter.get(router.join, getjoin);
globalRouter.post(router.join, postjoin);
globalRouter.get(router.login, getlogin);
globalRouter.post(router.login, postlogin);
globalRouter.get(router.logout, logout);
globalRouter.get(router.search, search);
globalRouter.get(router.loginhome, loginhome);

export default globalRouter;
