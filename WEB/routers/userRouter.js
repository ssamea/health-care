import express from "express";
import router from "../router";

const userRouter = express.Router();

userRouter.get(router.users, (req, res) => res.send("Users"));
userRouter.get(router.userDetail, (req, res) => res.send("Detail"));
userRouter.get(router.editProfile, (req, res) => res.send("Edit"));
userRouter.get(router.changePassword, (req, res) => res.send("ChangePassword"));

export default userRouter;
