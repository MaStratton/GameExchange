package org.GameExchange.ExchangeAPI.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ApplicationKafkaProducer {
    Properties props = new Properties();

    public ApplicationKafkaProducer(){
        props.put("bootstrap.servers", "VideoGameKafka:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    public void sendPasswdChange(int userId){
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            String key = "userId";
            String message = String.valueOf(userId);

            ProducerRecord<String, String> record = new ProducerRecord("password_change", key, message);

            producer.send(record);

            System.out.println("Sent Message");
            producer.close();
        } catch (Exception e){
            System.out.println("Unexpected Message Sender Exception: " + e.getMessage());
        }
    }

    public void sendOfferCreated(int offerer, int offeree){
        try (Producer<String, HashMap<String, String>> producer = new KafkaProducer<>(props)){
            String key = "Offer Created";
            Map<String, String> message = new HashMap<String, String>();
            message.put("Offerer", String.valueOf(offerer));
            message.put("Offeree", String.valueOf(offeree));


            ProducerRecord<String, HashMap<String, String>> record = new ProducerRecord("offer_created", key, message);

            producer.send(record);
        }catch (Exception e){
            System.out.println("Unexpected Message Sender Exception: " + e.getMessage());
        }

    }

    public void sendOfferUpdated(int offerer, int offeree, String Status){
        try (Producer<String, HashMap<String, String>> producer = new KafkaProducer<>(props)){
            String key = "Offer Created";
            Map<String, String> message = new HashMap<String, String>();
            message.put("Offerer", String.valueOf(offerer));
            message.put("Offeree", String.valueOf(offeree));


            ProducerRecord<String, HashMap<String, String>> record = new ProducerRecord("offer_created", key, message);

            producer.send(record);
        }catch (Exception e){
            System.out.println("Unexpected Message Sender Exception: " + e.getMessage());
        }
    }
}
