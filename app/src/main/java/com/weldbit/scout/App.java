/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.weldbit.scout;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import com.weldbit.algo.stack.Stack;
import com.weldbit.scout.storage.dao.DataAccessBase;
import com.weldbit.scout.storage.dao.TableLedgerDAO;
import com.weldbit.scout.storage.model.TableLedger;
import com.weldbit.scout.storage.service.DataStorage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://www.google.com/")).build();

        String htmldoc;
        htmldoc = client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).join();
        // System.out.println(htmldoc);
        Document doc = Jsoup.parse(htmldoc);

        String text = doc.body().text(); // "An example link"

        System.out.println(doc.title());

        List squares = new ArrayList();
        Flowable.range(1, 64) // 1
                .observeOn(Schedulers.computation()) // 2
                .map(v -> v * v) // 3
                .blockingSubscribe(squares::add); // 4

        for (Object object : squares) {
            System.out.println(object);
        }

        Stack<Integer> mystack = new Stack<Integer>();
        mystack.push(1);
        System.out.println(mystack.getTop());

        DataAccessBase tableLedger = new TableLedgerDAO();
        tableLedger.createTable();
        System.out.println(tableLedger.readTable());
        System.out.println("Stream below");
        tableLedger.readTable();

        TableLedger tLedger = new TableLedger();
        tLedger.setTablename("what filename");

        try (DataStorage<TableLedger> tblLedger = new DataStorage<>(tLedger, DataStorage.ACCESS_TYPE.WRITE)) {
            tblLedger.insert();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
