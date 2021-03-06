import firebase from "firebase";
import admin from "firebase-admin";
import serviceAccount from "./serviceKey.json";
import firestoreService from "firestore-export-import";

const config = {
  apiKey: "AIzaSyBH3WFCIoX2SGHo-JfpGd7AS7BsrxTW8Dw",
  authDomain: "dailyhealth-6f85c.firebaseapp.com",
  databaseURL: "https://dailyhealth-6f85c-default-rtdb.firebaseio.com",
  storageBucket: "dailyhealth-6f85c.appspot.com",
};

admin.initializeApp({
  config,
  credential: admin.credential.cert(serviceAccount),
});

export const db = admin.database();
