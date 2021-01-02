import express from "express";
import router from "../router";

const globalRouter = express.Router();

globalRouter.get(router.home, (req, res) => res.send("Home"));
globalRouter.get(router.join, (req, res) => res.send("Join"));
globalRouter.get(router.login, (req, res) => res.send("Login"));
globalRouter.get(router.logout, (req, res) => res.send("Logout"));

export default globalRouter;
