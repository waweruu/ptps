const express = require('express');
const cors = require('cors');
const prettyjson = require('prettyjson');
const bodyParser = require('body-parser');

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const app = express();
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));

app.post("/:licencePlate/:userUid/:start/:finish", (request, response) => {
  const entry = request.body.Body.stkCallback.CallbackMetadata.Item;
  const userUid = request.params.userUid;
  const start = request.params.start;
  const finish = request.params.finish;
  const licencePlate = request.params.licencePlate;
  let licArr = licencePlate.split('_');
  let numberPlate = licArr[0] + " " + licArr[1];
  let message = {
    "ResponseCode": "00000000",
	  "ResponseDesc": "success"
  };

  let amount = Object.values(entry)[0];
  let amountValue = amount.Value;

  let receipt = Object.values(entry)[1];
  let receiptValue = receipt.Value;

  let date = Object.values(entry)[3];
  let dateValue = date.Value;

  let phone = Object.values(entry)[4];
  let phoneValue = phone.Value;
  //console.log(amount);
  let amountNode = {
      "amount": amountValue,
      "mpesaReceiptNumber": receiptValue,
      "transactionDate": dateValue,
      "phoneNumber": phoneValue,
      "start": start,
      "finish": finish,
      "licencePlate": numberPlate
  };

  //let amount = entry.Item.Amount;
  //let receipt = entry.Item.MpesaReceiptNumber;
  //let date = entry.Item.TransactionDate;
  //let number = entry.Item.PhoneNumber;


  //let det = {};
  //det[number] = [];
  //det[number].push(

  //let det = {number: [{"amount": amount, "receiptNumber": receipt, "date": date}]};

  admin.database().ref('/Payments').child(userUid).child(receiptValue).set(amountNode)
    .then(() => {
      return response.json(message);
    }).catch(error => {
      console.error(error);
      return response.status(500).send('Oh no! Error: ' + error);
    });
});

exports.helloWorld = functions.https.onRequest((request, response) => {
  response.send("Hello from Firebase!");
});

exports.details = functions.https.onRequest(app);
