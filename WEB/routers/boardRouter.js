import express from "express";
import router from "../router";

const boardRouter = express.Router();

boardRouter.get(router.board, (req, res) => res.send("Board"));
boardRouter.get(router.boardUpload, (req, res) => res.send("Upload"));
boardRouter.get(router.boardDetail, (req, res) => res.send("Detail"));
boardRouter.get(router.editBoard, (req, res) => res.send("Edit"));

export default boardRouter;
