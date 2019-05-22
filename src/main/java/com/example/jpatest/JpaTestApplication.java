package com.example.jpatest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;

@SpringBootApplication
public class JpaTestApplication  implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JpaTestApplication.class, args);
    }

    @Autowired
   CategoryRepository cr;

    @Autowired
    ProductRepository pr;

    @Autowired
    FactoryRepository fr;

    @Override
    public void run(String... args) throws Exception {
        Category candy = new Category("candy");
        Category dairy = new Category("dairy");
        Product bar = new Product("chocolote bar");
        Product lollipop = new Product("lollipop");
        Product butter = new Product("butter");
        Factory factory = new Factory();
        Factory factory2 = new Factory();
        factory.setName("jeden");
        factory2.setName("dwa");

        fr.save(factory);
        fr.save(factory2);
        pr.save(lollipop);
        pr.save(bar);
        pr.save(butter);
        factory.getProducts().add(bar);
       // factory.getProducts().add(lollipop);
        factory2.getProducts().add(butter);
        factory2.getProducts().add(bar);
        bar.getFactorys().add(factory);
        bar.getFactorys().add(factory2);
        butter.getFactorys().add(factory2);
//        fr.save(factory);
//        fr.save(factory2);
//        pr.save(lollipop);
//        pr.save(bar);
//        pr.save(butter);
        fr.save(factory);
        fr.save(factory2);
        pr.save(lollipop);
        pr.save(bar);
        pr.save(butter);
        cr.flush();

      System.out.println(fr.findAll());
   //   System.out.println(pr.findAll());

    }
}
