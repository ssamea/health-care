import dotenv from "dotenv";
import * as firebase from "firebase/app";
import "firebase/database";
import "firebase/auth";
import admin from "firebase-admin";
import "firebase/firestore";
import serviceAccount from "./serviceKey.json";

dotenv.config();

const firebaseConfig = {
  apiKey: process.env.APIKEY,
  authDomain: process.env.AUTHDOMAIN,
  databaseURL: process.env.DATABASEURL,
  projectId: process.env.PROJECTID,
  storageBucket: process.env.STORAGEBUCKET,
  messagingSenderId: process.env.MESSAGINGSNEDERID,
  appId: process.env.APPID,
  measurementId: process.env.MEASUREMENTID,
};

firebase.initializeApp(firebaseConfig);

export const firebaseInstance = firebase;
export const realtimeService = firebase.database();
export const authService = firebase.auth();
export const dbService = firebase.firestore();
