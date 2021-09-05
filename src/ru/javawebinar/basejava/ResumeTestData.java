package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ResumeTestData {
    static Resume r = new Resume("uuid1", "name1");
    static List<String> list = new ArrayList<>(Arrays.asList("+7(921) 855-0482", "gkislin@yandex.ru", "grigory.kislin", "www.github.com", "www.stackoverflow.com", "www.ya.ru"));


    public static void main(String[] args) throws ParseException {
        for (ContactType ct : ContactType.values()) {
            r.contacts.setContact(ct, list.get(ct.ordinal()));
        }
        r.sections.put(SectionType.OBJECTIVE, new SimpleTextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        r.sections.put(SectionType.PERSONAL, new SimpleTextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        r.sections.put(SectionType.ACHIEVEMENT, new ListTextSection(new ArrayList<>(Arrays.asList(
                "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.",
                "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.",
                "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.",
                "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.",
                "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).",
                "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа."
        ))));
        r.sections.put(SectionType.QUALIFICATIONS, new ListTextSection(new ArrayList<>(Arrays.asList(
                "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
                "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,",
                "MySQL, SQLite, MS SQL, HSQLDB",
                "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy,",
                "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts,",
                "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).",
                "Python: Django.",
                "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js",
                "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka",
                "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.",
                "Инструменты: Maven + plugin development, Gradle, настройка Ngnix,",
                "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer.",
                "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования",
                "Родной русский, английский \"upper intermediate\""
        ))));

        DatedTextSection exp = new DatedTextSection();
        exp.add("Java Online Projects", new SimpleDateFormat("MM/yyyy").parse("10/2013"), null, "Автор проекта", "Создание, организация и проведение Java онлайн проектов и стажировок");
        exp.add("Wrike", new SimpleDateFormat("MM/yyyy").parse("10/2014"), new SimpleDateFormat("MM/yyyy").parse("01/2016"), "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.");
        exp.add("RIT Center", new SimpleDateFormat("MM/yyyy").parse("04/2012"), new SimpleDateFormat("MM/yyyy").parse("10/2014"), "Java архитектор", "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python");
        r.sections.put(SectionType.EXPERIENCE, exp);

        DatedTextSection edu = new DatedTextSection();
        edu.add("Coursera", new SimpleDateFormat("MM/yyyy").parse("03/2013"), new SimpleDateFormat("MM/yyyy").parse("05/2013"), "null", "Functional Programming Principles in Scala\" by Martin Odersky");
        edu.add("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", new SimpleDateFormat("MM/yyyy").parse("09/1993"), new SimpleDateFormat("MM/yyyy").parse("07/1996"), "null", "Аспирантура (программист С, С++)");
        edu.add("Заочная физико-техническая школа при МФТИ", new SimpleDateFormat("MM/yyyy").parse("09/1984"), new SimpleDateFormat("MM/yyyy").parse("06/1987"), "null", "Закончил с отличием");
        r.sections.put(SectionType.EDUCATION, new DatedTextSection());



        System.out.println(r.getFullName());
        for (ContactType ct : ContactType.values()) {
            System.out.print(ct.getTitle() + ": ");
            System.out.println(r.contacts.getContact(ct));
        }

        System.out.println();
        for (SectionType st : SectionType.values()) {
            System.out.println(st.getTitle());
            r.sections.get(st).view();
        }
    }


}
