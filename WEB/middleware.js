import router from "./router";

export const localsMiddleWare = (req, res, next) => {
  res.locals.siteName = "healthCare";
  res.locals.router = router;
  next();
};
