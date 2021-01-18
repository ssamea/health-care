import express from "express";
import router from "../router";
import {
  board,
  getBoardUpload,
  postBoardUpload,
  boardDetail,
  editBoard,
} from "../controllers/boardController";

const boardRouter = express.Router();

boardRouter.get(router.boardUpload, getBoardUpload);
boardRouter.post(router.boardUpload, postBoardUpload);
boardRouter.get(router.editBoard, editBoard);
boardRouter.get(router.boardDetail, boardDetail);

export default boardRouter;
