import express from "express";
import {
  users,
  changePassword,
  editProfile,
  userDetail,
} from "../controllers/userController";
import router from "../router";

const userRouter = express.Router();

userRouter.get(router.editProfile, editProfile);
userRouter.get(router.changePassword, changePassword);
userRouter.get(router.userDetail, userDetail);

export default userRouter;
