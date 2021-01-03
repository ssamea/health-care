import express from "express";
import {
  users,
  changePassword,
  editProfile,
  userDetail,
} from "../controllers/userController";
import router from "../router";

const userRouter = express.Router();

userRouter.get(router.users, users);
userRouter.get(router.userDetail, userDetail);
userRouter.get(router.editProfile, editProfile);
userRouter.get(router.changePassword, changePassword);

export default userRouter;
