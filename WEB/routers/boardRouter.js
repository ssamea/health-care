import express from "express";
import router from "../router";
import {
  board,
  boardUpload,
  boardDetail,
  editBoard,
} from "../controllers/boardController";

const boardRouter = express.Router();

boardRouter.get(router.board, board);
boardRouter.get(router.boardUpload, boardUpload);
boardRouter.get(router.editBoard, editBoard);
boardRouter.get(router.boardDetail, boardDetail);

export default boardRouter;
