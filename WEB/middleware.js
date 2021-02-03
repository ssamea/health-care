import router from "./router";
import { authService } from "./db";

export const localsMiddleWare = (req, res, next) => {
  res.locals.siteName = "healthCare";
  res.locals.router = router;
  res.locals.user = {
    isLoggedin: authService.currentUser,
  };
  next();
};

export default localsMiddleWare;
