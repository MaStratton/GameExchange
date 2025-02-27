//Kafka Setup
const { Kafka } = require("kafkajs");

const kafka = new Kafka({
    clientId: "VideoGameKafka",
    brokers: ["VideoGameKafka:9092"],
});

const consumer = kafka.consumer({ groupId: "test-group" });

//MySql Setup
var mysql = require("mysql2");

var con = mysql.createConnection({
    host: "VideoGameDB",
    user: "root",
    password: "password",
    database: "VideoGameExchange"
});


//Email Spoofer
const nodemailer = require("nodemailer");
const name = "Zola Grant";
const email = 'zola.grant45@ethereal.email';
const transporter = nodemailer.createTransport({
    host: 'smtp.ethereal.email',
    port: 587,
    auth: {
        user: 'zola.grant45@ethereal.email',
        pass: 'WWTymnt5sXxPAezyMu'
    }
});

//Kafka Consumer
const run = async () => {
    await consumer.connect();
    await consumer.subscribe({ topics: ["password_change", "offer_created", "offer_updated"], fromBeginning:false});

    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            console.log({
                value: message.value.toString()
            });
            console.log(topic);
            console.log(message.value.toString());
            if (topic === "password_change") {
                changePass(message.value.toString());
            } else if (topic === "offer_created") {
                offerCreated(message.value.toString());
            } else if (topic === "offer_updated") {
                offerUpdated(message.value.toString());
            } else {
                //console.log("Error. Invalid Topic");
            }
        },
    });
};

async function changePass(id){
    console.log("HIT")
    con.query(`SELECT emailAddr FROM People WHERE personId = ${id}`, (err, results, fields) => {
        console.log(results[0].emailAddr.toString());
        const info = transporter.sendMail({
            from: `"${name}" <${email}>`,
            to: results[0].emailAddr.toString(),
            subject: "Password Alert",
            text: "Your Password Has Been changed"
        });
        console.log(`Message Sent: ${info.messageId}`);
    });

}

async function offerUpdated(message){
    var status = new RegExp("Status=.+?[,}]").exec(message).toString().split('=').pop().slice(0, -1);
    var offerer = new RegExp("Offerer=.+?[,}]").exec(message).toString().split('=').pop().slice(0, -1);
    var offeree = new RegExp("Offeree=.+?[,}]").exec(message).toString().split('=').pop().slice(0, -1);
    console.log(offerer + " " + offeree + " " + status)
    con.query(`SELECT personId, emailAddr From People WHERE personId = ${offerer} OR personId = ${offeree}`, (err, results, fields) => {
        console.log(results);
        const info = transporter.sendMail({
            from: `"${name}" <${email}>`,
            to:[ results[0].emailAddr.toString(), results[1].emailAddr.toString()],
            subject: "Offer Updated",
            text: `An Offer you are part of has been updated to ${status}ed`
        });
    })

}

async function offerCreated(message){
    var offerer = new RegExp("Offerer=.+?[,}]").exec(message).toString().split('=').pop().slice(0, -1);
    var offeree = new RegExp("Offeree=.+?[,}]").exec(message).toString().split('=').pop().slice(0, -1);
    var status = 
    console.log(offerer + " " + offeree)
    con.query(`SELECT personId, emailAddr From People WHERE personId = ${offerer} OR personId = ${offeree}`, (err, results, fields) => {
        console.log(results);
        const info = transporter.sendMail({
            from: `"${name}" <${email}>`,
            to:[ results[0].emailAddr.toString(), results[1].emailAddr.toString()],
            subject: "Offer Created",
            text: `An offer has been created using some games under your account`
        });
    })

}



run().catch(console.error);