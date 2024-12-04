import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendJsonListEmail {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:getJsonList")
                        .bean(MyBean.class, "getJsonList")
                        .to("direct:sendMail");

                from("direct:sendMail")
                        .to("smtp://smtp.example.com?username=user&password=pass")
                        .setHeader("Subject", "JSON List")
                        .setHeader("Content-Type", "text/html")
                        .setBody(resource("classpath:json-list-template.html"))
                        .setHeader("CamelMailTemplateData", "${jsonList}");
            }
        });

        context.start();
        context.stop();

        // Test the route
        Map<String, Object> headers = new HashMap<>();
        headers.put("jsonList", getJsonList());
        context.createProducerTemplate().sendBodyAndHeaders("direct:getJsonList", null, headers);
    }

    public static List<Map<String, String>> getJsonList() {
        List<Map<String, String>> jsonList = new ArrayList<>();
        Map<String, String> item1 = new HashMap<>();
        item1.put("name", "John");
        item1.put("age", "30");
        jsonList.add(item1);

        Map<String, String> item2 = new HashMap<>();
        item2.put("name", "Jane");
        item2.put("age", "25");
        jsonList.add(item2);

        return jsonList;
    }

    public static class MyBean {
        public List<Map<String, String>> getJsonList() {
            return getJsonList();
        }
    }
}
