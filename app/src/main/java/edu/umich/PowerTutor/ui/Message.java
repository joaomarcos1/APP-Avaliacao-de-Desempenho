package edu.umich.PowerTutor.ui;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Message {

  public String userId;
  public String author;
  public String app1;
  public String app2;
  public String nome1;
  public String nome2;
  public long timestamp;
  public String MODEL         =   android.os.Build.MODEL;

    public Message() {
    // Default constructor required for calls to DataSnapshot.getValue(Message.class)
  }

  public Message(String userId, String author, String app1, String app2, String nome1, String nome2, long timestamp) {
    this.userId = userId;
    this.author = author;
    this.app1 = app1;
    this.app2 = app2;
    this.nome1 = nome1;
    this.nome2 = nome2;
    this.timestamp = timestamp;
  }

  @Exclude
  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<String, Object>();
    result.put("userId", userId);
    result.put("Autor", author);
    result.put(nome1, app1);
    result.put(nome2, app2);
      result.put("Modelo", MODEL);
    result.put("timestamp", timestamp);
    return result;
  }

  @Override public String toString() {
    return "Message{" +
        "Autor='" + author + '\'' +
        ", userId='" + userId + '\'' +
        nome1 + ",='" + app1 + '\'' +
            nome2 + ",='" + app2 + '\'' +
            ", Modelo='" + MODEL + '\'' +
        ", timestamp=" + timestamp +
        '}';
  }
}