package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MainCollections {

    public static final String UUID_1 = "uuid1";
    public static final Resume RESUME1 = new Resume(UUID_1);
    public static final String UUID_2 = "uuid2";
    public static final Resume RESUME2 = new Resume(UUID_2);
    public static final String UUID_3 = "uuid3";
    public static final Resume RESUME3 = new Resume(UUID_3);
    public static final String UUID_4 = "uuid4";
    public static final Resume RESUME4 = new Resume(UUID_4);

    public static void main(String[] args) {
        Collection<Resume> collection = new ArrayList<>();
        collection.add(RESUME1);
        collection.add(RESUME2);
        collection.add(RESUME3);
        collection.add(RESUME4);

        for (Resume r : collection) {
            System.out.println(r);
            if (Objects.equals(r.getUuid(), "uuid1")) {
//                collection.remove(r);
            }
        }

        Iterator<Resume> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resume r = iterator.next();
            if (Objects.equals(r.getUuid(), "uuid1")) {
                iterator.remove();
            }
        }
        System.out.println(collection);

        System.out.println("\n=====MAP");
        Map<String, Resume> map = new HashMap<>();
        map.put(UUID_1, RESUME1);
        map.put(UUID_2, RESUME2);
        map.put(UUID_3, RESUME3);

        for(String uuid : map.keySet()) {
            System.out.println(map.get(uuid));
        }

        for (Map.Entry<String, Resume> entry : map.entrySet()) {
            System.out.println(entry.getValue());
        }

        List<String> list = new ArrayList<>(2);
        for (int i = 1; i < 4; i++) {
            list.add(String.valueOf(i));
        }
        System.out.println(list);

        List<String> list1 = new ArrayList<>();
        list1.add("111");
        list1.add("222");
        System.out.println(list1);

        System.out.println(list1);
    }
}
