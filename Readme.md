
**States**
- transient  `
 Student student = new Student(); 
`
- persist ``
session.save(student);
``
- detach - *detached instance is an object that has been persistent, but its Session has been closed*.



## @OneToMany

**Unidirectional**

```java
public class Product {}
public class Category {  
  @OneToMany(fetch = FetchType.EAGER)  
 private List<Product> products = new LinkedList<>();
 }
 
 CategoryRepository cr;  
 ProductRepository pr;

pr.save(bar);  
pr.save(lollipop);  
candy.getProducts().add(bar);  
candy.getProducts().add(lollipop);  
cr.save(candy);
 ```   
 *saveów jest sporo bo nie ma adnotacji @Cascade (inaczej leciałby not transient)

```sql
Hibernate: insert into product (name, id) values (?, ?)
Hibernate: insert into product (name, id) values (?, ?)
Hibernate: insert into category (name, id) values (?, ?)
Hibernate: insert into category_products (category_id, products_id) values (?, ?)
Hibernate: insert into category_products (category_id, products_id) values (?, ?)
// usnięto logi związane z id
 ```
 - niezbyt wydajne mamy trzy tabele zamiast dwóch 


**Unidirectional  with @JoinColumn**

```java
public class Product {// nie zadeklarowano pola p_id}
public class Category {  
  @OneToMany(fetch = FetchType.EAGER)  
  @JoinColumn(name = "p_id")
 private List<Product> products = new LinkedList<>();
 }
 // save the same
 ```   

```sql
Hibernate: insert into product (name, id) values (?, ?)
Hibernate: insert into product (name, id) values (?, ?)
Hibernate: insert into category (name, id) values (?, ?)
Hibernate: update product set cat_id=? where id=?
Hibernate: update product set cat_id=? where id=?
```
*cat_id == p_id // zmiana zmiennych
- dziecko przechowuje id rodzica
- update wstawia id rodzica

**Bidirectional**

```java
public class Product {
	@ManyToOne()  
	@JoinColumn(name = "cat_id")  
     private Category category;
}
public class Category {  
  @OneToMany(mappedBy = "category")
 private List<Product> products = new LinkedList<>();
 }

cr.save(candy);  
  bar.setCategory(candy);  
  lollipop.setCategory(candy);  
  pr.save(bar);  
  pr.save(lollipop);  
  candy.getProducts().add(bar);  
  candy.getProducts().add(lollipop);  

 ```   

- Zapis danych 
	- należy najpierw zapisać rodzica,  // nie może być transient przy przypisywaniu do dziecka 
	-  do dziecka przypisać rodzica // inaczej cat_id będzie null
	-  zapisać dzieci // wymagane ze względu na cascade
	-  do rodzica dodać dzieci

```sql 
Hibernate: insert into category (name, id) values (?, ?)
Hibernate: insert into product (p_id, name, id) values (?, ?, ?)
Hibernate: insert into product (p_id, name, id) values (?, ?, ?)
```


- dziecko przechowuje id rodzica
- najefektywniejsza
- dobrą praktyką jest zaimplementowanie equals i hashcode w klasie dziecka
- @JoinColumn w tym wypadku jedynie zmienia nazwe kolumny, nawet przy jego barku zostaną wygenerowane dwie tabele
- proponowane jest stworzenie metody w klasie rodzica
```java
public void addComment(PostComment comment) {
comments.add(comment);
comment.setPost(this);}
```

**Problemy**
- nie możemy ograniczyc liczby zwracanych recordów 
> Therefore, in reality, `@OneToMany` is practical only when many means few. Maybe `@OneToFew` would have been a more suggestive name for this annotation.


## @ManyToMany
