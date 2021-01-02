import { Router } from "express";

// Global Routes
const HOME = "/";
const JOIN = "/join";
const LOGIN = "/login";
const LOGOUT = "/logout";

// User Router
const USERS = "/users";
const USER_DEATIL = "/:id"; // User에게 id를 배정.
const EDIT_PROFILE = "/edit-profile";
const CHANGE_PASSWORD = "/change-password";

// Boards
const BOARD = "/board";
const BOARD_UPLOAD = "/upload";
const BOARD_DETAIL = "/:id";
const EDIT_BOARD = "/:id/edit";

const router = {
  home: HOME,
  join: JOIN,
  login: LOGIN,
  logout: LOGOUT,
  users: USERS,
  userDetail: USER_DEATIL,
  editProfile: EDIT_PROFILE,
  changePassword: CHANGE_PASSWORD,
  board: BOARD,
  boardUpload: BOARD_UPLOAD,
  boardDetail: BOARD_DETAIL,
  editBoard: EDIT_BOARD,
};

export default router;
