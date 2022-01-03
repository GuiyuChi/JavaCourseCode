# IOC/DI

## 一、注入（Injection）


### 1.什么是注入？


`通过Spring工厂极其组件，为Bean赋值`


#### 1.1 为什么要有注入？


直接通过代码注入，有耦合

#### 1.2如何进行注入？


- 为成员变量编写get set方法
- 通过spring配置文件赋值



#### 1.3 注入的好处


`解耦合`


### 2. 注入的原理


`Spring读取配置文件取值，再调用对象的属性的set方法，完成成员变量的赋值`


这种方式称为`set注入`


## 二、Set注入详解


对象的成员变量可能有多种类型，可分为**JDK内置类型**和**用户自定义类型**


### 1.JDK内置类型


#### 1.1 八种基本类型+String


value标签


```xml
<value>xxx</value>
```


#### 1.2数组


value标签外嵌套list标签


```xml
<list>
  <value>abc</value>
  <value>123</value>
</list>
```


#### 1.3 set集合


用set标签.


1. set集合两大特点： **无序**  **不可重复**
1. set标签内，如果set成员变量没有泛型约束，可用任意赋值标签, 值、数组、bean都可以



```xml
<set>
	<value>911</value>
	<value>110</value>
	<ref bean="">beanName</value>
</set>
```


#### 1.4 list集合


同数组，用list标签


1. list集合：**有序**   **可重复**
1. 相比数组，list内可嵌套多种类型



#### 1.5 map


用map标签，里面再套entry标签，存键值对，一个entry就是一对键值对。


```xml
            <map>
                <entry>
                    <key>
                        <value>sky</value>
                    </key>
                    <value>8707237</value>
                </entry>
                <entry>
                    <key>
                        <value>ming</value>
                    </key>
                    <value>185151</value>
                </entry>
            </map>
```


#### 1.6 Properties


用props标签


```xml
<props>
	<prop key="key1">value1</prop>
  <prop key="key2">value2</prop>
</props>
```


#### 1.7 复杂JDK类型，如Date类型


需要自定义类型转换器进行处理


### 2.自定义类型


#### 2.1 方式一


1. 为成员变量提供set get 方法
1. 配置文件中为成员变量赋值（注入）



#### 2.1方式二


方式一中存在的问题：


- 配置文件中，赋值代码冗余
- 被注入对象，多次创建，浪费资源



1. 为成员变量提供get/set方法
1. 用标签配置bean



### 3. Set注入的简化写法


1. 基于value属性简化
1. 通过命名空间p简化



## 三、构造注入


```java
1.注入
2.set注入
3.构造注入
```


### 1. 使用方法


1. 定义构造函数
1. 配置文件中进行配置
用标签为参数赋值，一个标签严格对应一个参数
```xml
<bean id="customer" class="indi.sky.basic.constructor.Customer">
        <constructor-arg>
            <value>skyming</value>
        </constructor-arg>
        <constructor-arg>
            <value>18</value>
        </constructor-arg>
    </bean>
```


### 2. 构造方法的重载


```markdown
重载：方法名一样，参数表不同
```


#### 2.1构造函数参数个数不同


#### 2.2构造函数参数个数相同


### 3. set注入与构造注入的比较


## 四、反转控制 与 依赖注入


### 1. 反转控制 IOC Inverse of Control


##### 控制
对成员变量赋值的控制权。
##### 反转控制
把对成员变量赋值的控制权，从**代码**中反转到**Spring配置文件和工厂**
##### 底层实现
工厂设计模式


常规的对成员变量赋值的控制权在**代码**中，有耦合；spring将对成员变量赋值的控制权转移到**Spring配置文件和工厂**中


### 2.依赖注入 DI Dependency Injection


##### 注入
通过Spring的配置文件和工厂，为对象（Bean、组件）的成员变量赋值
##### 依赖注入
就是一个类需要另一个类，一旦出现依赖，就可以把另一个类作为当前类的成员变量值，最终通过Spring配置文件进行注入（赋值）


## 五、Spring（工厂）创建复杂对象


### 1.复杂对象


```markdown
复杂对象：不能直接通过new构造方法创建的复杂对象
```


### 2.Spring工厂创建复杂对象的3种方式


#### 2.1 FactoryBean接口


- 开发步骤
   - 实现FactoryBean接口
```java
public class ConnectionFactoryBean implements FactoryBean {
    // 创建复杂对象的代码
    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

  	/*
     是否单例
     return false : 每次都创建一个新的对象
     return true ： 只会创建一个对象，即单例模式
     */
    @Override
    public boolean isSingleton() {
        return false;
    }
}
```

      - Spring配置文件的配置
```xml
<bean id="conn" class="indi.sky.basic.factoryBean.ConnectionFactoryBean"></bean>
```

      - 调用 ctx.getBean("conn")获取Connection
**注意：**这获取的不是`ConnectionFactoryBean`对象,而是`ConnectionFactoryBean`对象中`getObject()`方法返回的类型·
```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
// 获取的是ConnectionFactoryBean中getObject()方法返回的对象
Connection connection = ((Connection) ctx.getBean("conn"));
```

- 细节：
   - 如果想获得`FactoryBean`对象（此例子中是`ConnectionFactoryBean`），需要加个`&`符号
```java
// 加了个&符号，获取的是ConnectionFactoryBean对象本身
ConnectionFactoryBean factoryBean = ((ConnectionFactoryBean) ctx.getBean("&conn"));
```

   - isSingleton()
      - 返回true  启用单例模式，只会创建一个对象
      - 返回false 每次都会创建一个新对象
- FactoryBean的实现原理[简易版]
   - 运行流程
      1. 通过("conn")获得`ConnectionFactoryBean`类的对象，再通过`instanceOf`判断是否是`FactoryBean`接口的实现类
      1. 如果是`FactoryBean`接口的实现类，就执行`getObject()`方法
      1. 返回`Connection`对象



#### 2.2 实例工厂


```markdown
1. 避免Spring框架的侵入
2. 整合遗留系统
```


#### 2.3 静态工厂


```markdown
将获取对象的方法定义为static方法
```


### 3.总结


![](/Users/sky-mbp16/Documents/LearnSpring/image-20200712224652032.png#align=left&display=inline&height=358&margin=%5Bobject%20Object%5D&originHeight=358&originWidth=1718&status=done&style=none&width=1718)


## 六、控制Spring工厂创建对象的次数


### 1.控制创建简单对象的次数


```xml
<bean id="account" scope="singleton" class="indi.sky.basic.scope.Account"></bean>
```


设置bean标签scope属性


- singleton(默认值)：只创建一次
- prototype：每次都创建一个新的对象



### 2.控制复杂对象的创建次数


指定FactoryBean接口的isSingleton()方法返回值


- 返回true  启用单例模式，只会创建一个对象
- 返回false 每次都会创建一个新对象



### 3.为什么要控制对象的创建次数


创建次数选择策略


- 只创建一次的对象
   - SqlSessionFactory
   - Dao
   - Service
- 每次都需要创建新的
   - Connection
   - SqlSession | Session



## 七、对象的生命周期


### 1.什么是对象的生命周期


```markdown
对象创建、存货、消亡的完整过程
```


### 3.对象生命周期的3个阶段


#### 1.创建


- 何时创建？
   - scope="singleton", **Spring工厂创建的同时，创建对象**
```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
// Spring工厂创建的同时，创建对象
```

   - scope="prototype", **Spring工厂获取对象时，才创建对象**
```java
// 创建工厂
ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
// 获取对象
Product product = ctx.getBean(Product.class);
//Spring工厂获取对象时，才创建对象
```

   - 特殊配置
`scope="singleton"`时，设置`azy-init="true"`，可以实现对象的懒加载
```xml
<bean id="product" scope="singleton" class="indi.sky.basic.life.Product" lazy-init="true">
```

   - 此时，对象在工厂获取对象时才被创建



#### 2.初始化


Spring工厂创建对象后，调用对象的初始化方法。


- 初始化方法提供：自定义方法
- 初始化方法调用：Spring调用



**实现方式：**


1. 实现`InitializingBean`接口，重写`afterPropertiesSet`方法



```java
public class Product implements InitializingBean {
    public Product() {
        System.out.println("Product.Product");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Product.afterPropertiesSet");
    }
}
```


2. 自定义一个普通方法，并在配置文件中指定为初始化方法
```java
protected void myInitMethod()  {
        System.out.println("Product.myInitMethod");
}

<bean id="product1" init-method="myInitMethod" class="indi.sky.basic.life.Product"></bean>
```


**细节：**


- 如果两个方式都实现了，则顺序
```java
1.afterPropertiesSet
2.自定义普通方法
```

- 先执行注入（为属性赋值）方法，再执行初始化方法



#### 3.销毁


Spring对象销毁前，会调用对象的销毁方法


- 何时销毁对象
Spring工厂销毁时：`ctx.close()`
- 谁来调用销毁方法
Spring工厂



**实现方式**


1. 实现`DisposableBean`接口，重写`destroy`方法
1. 自定义普通方法，在spring配置文件中为bean配置此销毁方法



```java
public class Product implements DisposableBean {
    public Product() {
        System.out.println("Product.Product");
    }

  // 后执行
    @Override
    public void destroy() throws Exception {
        System.out.println("Product.destroy");
    }

  // 先执行
    public void myDestroy() {
        System.out.println("Product.myDestroy");
    }
  
  //  <bean id="product2" class="indi.sky.basic.life.Product" destroy-method="myDestroy"></bean>

}
```


**细节**


- 销毁操作只适用于`scope="singleton"`时，其他情况，对象可销毁但不调用销毁方法



#### Spring中Bean实例化过程


![](/Users/sky-mbp16/Documents/LearnSpring/Spring%E4%B8%ADBean%E5%AE%9E%E4%BE%8B%E5%8C%96%E8%BF%87%E7%A8%8B.png#align=left&display=inline&height=642&margin=%5Bobject%20Object%5D&originHeight=642&originWidth=1552&status=done&style=none&width=1552)


#### 声明周期总结


![](/Users/sky-mbp16/Documents/LearnSpring/image-20200727215237187.png#align=left&display=inline&height=1162&margin=%5Bobject%20Object%5D&originHeight=1162&originWidth=2904&status=done&style=none&width=2904)


## 八、类型转换器


### 1. 类型转换器介绍


### 2. 自定义类型转化器


开发步骤：


1. 创建自定义类型转换器对象，并实现`convert`接口
```java
public class MyDataConverter implements Converter<String, Date> {
    /*
    字符串类型 =》Date类型
     */
    @Override
    public Date convert(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
```

2. 配置Spring配置文件
2.1 创建类型转化器对象
```xml
    <!--    创建自定义类型转换器对象-->
		<bean id="convert" class="indi.sky.basic.converter.MyDataConverter"/>
```

2. 2.2 注册类型转换器
```xml
	  <!--    注册类型转换器-->
    <bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
        <property name="converters">
            <set>
                <ref bean="convert"/>
            </set>
        </property>
    </bean>
```


## 九、后置处理Bean


```markdown
BeanPostProcessor的作用：对Spring工厂创建的对象，进行再加工
```


![](/Users/sky-mbp16/Documents/LearnSpring/beanPost%E8%BF%87%E7%A8%8B.png#align=left&display=inline&height=1208&margin=%5Bobject%20Object%5D&originHeight=1208&originWidth=2782&status=done&style=none&width=2782)


#### BeanPostProcessor开发步骤


1. 实现`BeanPostProcessor`接口
```java
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
   
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Categroy categroy = (Categroy) bean;
        categroy.setName("skyming666");
        return categroy;
    }
}
```

2. 配置Spring配置文件
```xml
<bean id="myBeanPostProcessor" class="indi.sky.basic.beanpost.MyBeanPostProcessor"/>
```


#### 细节

`BeanPostProcessor`对所有的由Spring工厂创建的对象都有效，一旦配置了`BeanPostProcessor`，所有由Spring工厂创建的对象都会执行`BeanPostProcessor`的



# AOP

## 一、静态代理设计模式


### 1. 为什么需要代理设计模式


### 2. 代理设计模式


#### 1.1 概念


```markdown
通过代理类，为原始类（目标）增加额外的功能
好处：保护原始类，额外功能易维护
```


#### 1.2名词


- 目标类（原始类）
- 目标方法（原始方法）
- 额外功能



#### 1.3 代理开发的核心


代理类 = 原始类 + 额外功能 + **实现原始类所实现的接口**


- 原始类
- 额外功能
- **实现原始类所实现的接口**



#### 1.4 开发过程


##### 静态代理


```markdown
一个原始类对应一个代理类
需要为每一个原始类写一个对应的代理类
```


```java
// 代理类
// 实现原始类所实现的接口
public class UserServiceProxy implements UserService {
  	// 原始类
    private UserServiceImpl userService = new UserServiceImpl();

    @Override
    public void register(User user) {
        System.out.println("------------额外功能--开始------------");
      	// 原始方法
        userService.register(user);
    }

    @Override
    public boolean login(String userName, String passWord) {
        System.out.println("------------额外功能--开始------------");
        return userService.login(userName,passWord );
    }
}
```


#### 1.5静态代理存在的问题


- 类文件成倍增加，不利于项目维护
- 额外功能维护性差



## 二、动态代理设计模式


### 1.概念


### 2. 引入依赖


### 3.开发过程


1. 创建原始类
1. 创建对象
1. Spring配置文件里声明bean
1. 额外功能

```java
// 实现MethodBeforeAdvice方法，并在before()方法中定义功能
public class Before implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("----------method before advice log-----------");
    }
}
```

```xml
// Spring配置文件中声明bean
<bean id="before" class="com.sky.aop.dynamic.Before"/>
```

5. 定义切点
   切点：额外功能执行的地方
   `在Spring文件中定义切点`
5. 整合

```xml
<aop:config>
    // 定义切点
    <aop:pointcut id="pc" expression="execution(* *(..))"/>
		// 组合额外功能与切点
  	// advice-ref
  	// pointcut-ref
    <aop:advisor advice-ref="before" pointcut-ref="pc"></aop:advisor>
</aop:config>
```


## 三、Spring动态代理详解


### 1. 实现额外功能


#### MethodBeforeAdvice接口


1. 作用：
   添加运行在目标方法之前执行的方法
1. before()方法：

```java
/**
 * @param method 
 * @param args 
 * @param target 
 *
 */

/**
 * Callback before a given method is invoked.
 * @param method method being invoked
 * @param args arguments to the method
 * @param target target of the method invocation. May be {@code null}.
 * @throws Throwable if this object wishes to abort the call.
 * Any exception thrown will be returned to the caller if it's
 * allowed by the method signature. Otherwise the exception
 * will be wrapped as a runtime exception.
 */
void before(Method method, Object[] args, @Nullable Object target) throws Throwable;
```


#### MethodInterceptor接口（方法拦截器）


```java
@FunctionalInterface
public interface MethodInterceptor extends Interceptor {

   /**
    * Implement this method to perform extra treatments before and
    * after the invocation. Polite implementations would certainly
    * like to invoke {@link Joinpoint#proceed()}.
    * @param invocation the method invocation joinpoint
    * @return the result of the call to {@link Joinpoint#proceed()};
    * might be intercepted by the interceptor
    * @throws Throwable if the interceptors or the target object
    * throws an exception
    */
   Object invoke(MethodInvocation invocation) throws Throwable;

}
```


1. invoke()方法
   执行额外功能，可运行在目标方法前或后
1. invocation参数
   method，就是目标方法本身的Method对象
   `invocation.proceed()`执行目标方法
1. 返回值
   `invocation.proceed()`
   原始方法的返回值作为invoke()方法的返回值

```java
public class Arround implements MethodInterceptor {    @Override    public Object invoke(MethodInvocation invocation) throws Throwable {        Object ret = invocation.proceed();        return ret;    }}
```

4. **注意：**
   MethodInterceptor中，需要手动执行目标方法，而MethodBeforeAdvice不用



### 2.详解切点


```xml
execution(* *(..))execution() 切点函数* *(..) 切点表达式
```


#### 2.1 切点表达式


以`public void add(int a,int b)`和`* *(..)`为例


第一个`*`代表方法修饰符  如：``public void`


第二个`*`达标方法名 如 `add`


括号里代表参数列表，`..`代表对参数个数、类型都没有要求


![](/Users/sky-mbp16/Documents/LearnSpring/切点表达式.png%23align=left&display=inline&height=1236&margin=[object Object]&originHeight=1236&originWidth=2698&status=done&style=none&width=2698)


##### 1. 方法切点


##### 2.  类切点


##### 3.  包切点


#### 2.2 切点函数


##### 1. exc  ution()


功能最全的切点函数，配合切点表达式可表示所有起点


##### 2. args()


用于参数匹配


```java
// 匹配两个string参数的方法excution(* *(String,String));args(String,String);
```


##### 3.within()


用于类、包的匹配


```java
// 匹配userService这个类excution(* *..userService.*(..));within(*..userService);// 匹配包excution(* com.sky.basic.proxy..*.*(..));within(com.sky.basic.proxy..*)
```


##### 4. [@annotation ](/annotation )


匹配具有**特殊注解**的方法


```java
// 自定义注解Log@Target(ElementType.METHOD)@Retention(RetentionPolicy.RUNTIME)public @interface Log {}// 配置切点<aop:pointcut id="annotationPc" expression="@annotation(com.sky.aop.Log)"/>
```


##### 5. 切点函数逻辑运算


- and 与运算

```xml
<aop:pointcut id="pc" expression="execution(* login(..)) and args(String,String)"/>匹配同时满足这两个条件的execution(* login(..))args(String,String)
```

- 注意：and运算只能使用不同类型的切点函数
- or 或运算

```xml
<aop:pointcut id="pc" expression="execution(* login(..)) or execution(* login(..))"/>二者满足其一即可
```


## 四、AOP编程


### 1. 概念


AOP: Aspect Oriented Programming


## 五、AOP底层实现原理


### 1. 核心问题


#### 【重要】1. AOP如何创建动态代理类？


(动态字节码)


#### 【重要】2. Spring工厂如何加工创建代理对象


（通过原始对象所配置的id, 获取到的是代理对象）


原理：参考Spring工厂创建对象的过程，Spring工厂获取对象时，在BeanPostProcessor中创建代理对象并返回


### 2. 动态代理类的创建


#### 2.1 JDK的动态代理


原理：通过**接口**创建代  理的实现类


```java
public static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)
```


#### 2.2 CGLIB的动态代理


原理：通过父子**继承**，代理对象继承原始对象。这样保证二者方法一致，同时能够在代理对象中实现新的功能（额外功能+原始方法）


## 六、基于注解的AOP编程


### 1.开发过程


```java
@Aspectpublic class MyAspect {		// 切点复用    @Pointcut("execution(* com.sky.aspect.UserServiceImpl.*(..))")    public void myPoincut(){};    @Around("execution(* com.sky.aspect.UserServiceImpl.*(..))")    public Object aroud(ProceedingJoinPoint joinPoint) throws Throwable {        System.out.println("---------额外功能    MyAspect---------");        Object ret = joinPoint.proceed();        return ret;    }    @Around("myPoincut()")    public Object aroud1(ProceedingJoinPoint joinPoint) throws Throwable {        System.out.println("---------额外功能    MyAspect---------");        Object ret = joinPoint.proceed();        return ret;    }}
```


### 2.细节


#### AOP中，动态代理的实现方式


```xml
<aop:aspectj-autoproxy proxy-target-class="false"/>
```
