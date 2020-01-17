`@ComponentScans`：用来包含多个@ComponentScan

```java
@ComponentScans({@ComponentScan("..."), @ComponentScan("...")})
```

```java
@ComponentScan(basePackageClasses = "com.abc",  useDefaulters = true)
```

```java
@ComponentScan(basePackageClasses = "com.cdf",  useDefaulters = fale)
```

同一个`@ComponentScan` 只能在一个类上使用一次，所以需要将上述两个合并如下

```java
@ComponentScans({
    @ComponentScan(basePackageClasses = "com.cdf",  useDefaulters = fale),
    @ComponentScan(basePackageClasses = "com.abc",  useDefaulters = true)
})
```

```java
protected final ConfigurationClassParser.SourceClass doProcessConfigurationClass(ConfigurationClass configClass, ConfigurationClassParser.SourceClass sourceClass) throws IOException {
        this.processMemberClasses(configClass, sourceClass);
        Iterator var3 = AnnotationConfigUtils.attributesForRepeatable(sourceClass.getMetadata(), PropertySources.class, PropertySource.class).iterator();

        AnnotationAttributes importResource;
        while(var3.hasNext()) {
            importResource = (AnnotationAttributes)var3.next();
            if (this.environment instanceof ConfigurableEnvironment) {
                this.processPropertySource(importResource);
            } else {
                this.logger.warn("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() + "]. Reason: Environment must implement ConfigurableEnvironment");
            }
        }
		//扫描得到所有的@ComponentScan注解
        Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
        if (!componentScans.isEmpty() && !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
            Iterator var13 = componentScans.iterator();

            while(var13.hasNext()) {
                AnnotationAttributes componentScan = (AnnotationAttributes)var13.next();
                /** BeanDefinitionHolder:
                *	private final BeanDefinition beanDefinition;
                *    private final String beanName;
                *   @Nullable
                *   private final String[] aliases;
                */
                Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
                Iterator var7 = scannedBeanDefinitions.iterator();

                while(var7.hasNext()) {
                    BeanDefinitionHolder holder = (BeanDefinitionHolder)var7.next();
                    BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
                    if (bdCand == null) {
                        bdCand = holder.getBeanDefinition();
                    }

                    if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
                        this.parse(bdCand.getBeanClassName(), holder.getBeanName());
                    }
                }
            }
        }

        this.processImports(configClass, sourceClass, this.getImports(sourceClass), true);
        importResource = AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
        if (importResource != null) {
            String[] resources = importResource.getStringArray("locations");
            Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
            String[] var19 = resources;
            int var21 = resources.length;

            for(int var22 = 0; var22 < var21; ++var22) {
                String resource = var19[var22];
                String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
                configClass.addImportedResource(resolvedResource, readerClass);
            }
        }

        Set<MethodMetadata> beanMethods = this.retrieveBeanMethodMetadata(sourceClass);
        Iterator var17 = beanMethods.iterator();

        while(var17.hasNext()) {
            MethodMetadata methodMetadata = (MethodMetadata)var17.next();
            configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
        }

        this.processInterfaces(configClass, sourceClass);
        if (sourceClass.getMetadata().hasSuperClass()) {
            String superclass = sourceClass.getMetadata().getSuperClassName();
            if (superclass != null && !superclass.startsWith("java") && !this.knownSuperclasses.containsKey(superclass)) {
                this.knownSuperclasses.put(superclass, configClass);
                return sourceClass.getSuperClass();
            }
        }

        return null;
    }
```

- `@ComponentScans` 整个流程

`@ComponentScan` -> `@Configuration` class-> basePackages -> `@Component` Beans -> `BeanDefinition` -> `BeanDefinitionRegistry` -> Beans -> `BeanFactory` -> getBean or `@Autowire`

- Bean的生命周期

1. 实例化

   Bean Class -> Bean Object

2. 初始化前: Bean before/pre(init) ---BeanPostProcessor

   - `postProcessBeforeInitialization(Object bean, String beanName)`

3. 初始化

   - `InitiallizingBean # AfterPropertiesSet()`

4. 初始化后

   - `BeanPostProcessor`

   - ​	`postProcessAfterInitialization`

5. 销毁

   - `Disposable#destroy() `

## Thymeleaf 视图技术

UI友好，前端可以立马看到效果

### 渲染上下文（model）模型

- spring web mvc

  - [ ] 接口类型

  - Model

  - ModelMap

  - ModelAndView
    - Model -》 ModelMap
    - View
  - 注解类型
    - ModelAttribute

### EL表达式

- 字符

- 多种数据类型

- 逻辑表达式

  if else

- 迭代表达式

  for each/while

- 反射

  java reflection

  cglib

#### Spring MVC 模板渲染逻辑

springmvc 核心总控制器： `DispatcherServlet`

- C	`DispatcherServlet`
- M      
  - Spring MVC(部分)
    - Model/ModelMap/ModelAndView(Model部分)
    - `@ModelAttribute`

  - 模板引擎（通常）

    - 通用的方式

      - 模板上下文
        - 内嵌/内建自己的工具变量

    - JSP九大内置变量
      - Servlet Scope上下文（Spring 中的 @Scope， Bean的生命周期和范围 ）

      - ```java
         public static void registerWebApplicationScopes(ConfigurableListableBeanFactory beanFactory, @Nullable ServletContext sc) {
                beanFactory.registerScope("request", new RequestScope());
                beanFactory.registerScope("session", new SessionScope());
                if (sc != null) {
                    ServletContextScope appScope = new ServletContextScope(sc);
                    beanFactory.registerScope("application", appScope);
                    sc.setAttribute(ServletContextScope.class.getName(), appScope);
                }
                beanFactory.registerResolvableDependency(ServletRequest.class, new WebApplicationContextUtils.RequestObjectFactory());
                beanFactory.registerResolvableDependency(ServletResponse.class, new WebApplicationContextUtils.ResponseObjectFactory());
                beanFactory.registerResolvableDependency(HttpSession.class, new WebApplicationContextUtils.SessionObjectFactory());
                beanFactory.registerResolvableDependency(WebRequest.class, new WebApplicationContextUtils.WebRequestObjectFactory());
                if (jsfPresent) {
                    WebApplicationContextUtils.FacesDependencyRegistrar.registerFacesDependencies(beanFactory);
                }
            }
        ```

        - PageContext（Page变量）
          - 关注当前页面：A forward B，A的变量只能在A使用
        - Request（请求上下文）
          - 关注于当前请求： A forward B， A变量可以用于B页面
        - Session（会话上下文）
          - 关注于当前会话， A forward/redirect B, A的变量可以用在B页面
        - ServletContext（应用上下文）
          - 关注当前应用：可以会话共享

      - JSP内置变量（JSP = Servlet）
        - out（Writter = ServletResponse#getWriter）
        - Exception（Throwable）
        - config（ServletConfig）
        - page（JSP Servlet对象）
        - response（ServletResponse）

    - V

      - 视图对象

        - servlet
          - RequestDispatcher.forward
          - RequestDispatcher.include
          - HttpServletResponse.sendRedirect
        - Spring MVC
          - View
            - forward
              - RequestDispatcher.forward
              - InternalResource View
            - redirect
              - RedirectView
        - Struts
          - Action
            - ForwardAction
            - RedirectAction

      - 视图处理对象

        - Spring MVC

          - *.do -> DispatcherServlet -> Controller -> View -> ViewResolver -> View.render -> HTML -> HttpServletResponse
            - Thymeleaf	ViewResolver ->  ThymeleafViewResolver
            - Thymeleaf View -> ThymeleafView
              - 通过模板解析模板资源（ClassPathResouce）
                - `TemplateResolution`
              - 读取资源，并渲染内容HTML
                - `IEngineContext`
                - ProceeTemplateHandler
              - HTML 内容输出到response
            - 源码：`org.thymeleaf.TemplateEngine.process(org.thymeleaf.TemplateSpec,org.thymeleaf.context.IContext, java.io.Writer)`
            - `org.thymeleaf.engine.TemplateManager.parseAndProcess`

        - Struts

          - *.do-> ActionServlet -> Action -> ForwardAction -> RequestDispatcher -> Servlet(jsp) ->  HTML -> HttpServletResponse

            - JSP的实现

              - 传统配置

                - ```xml
                  <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
                      <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
                      <property name="prefix" value="/WEB-INF/jsp/"/>
                      <property name="suffix" value=".jsp"/>
                  </bean>
                  ```

              - ViewResolver -> InternalViewResovler

              - `InternalViewResolver`

              - `JstlView`

doDispatch方法

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest = request;
        HandlerExecutionChain mappedHandler = null;
        boolean multipartRequestParsed = false;
        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

        try {
            try {
                ModelAndView mv = null;
                Object dispatchException = null;

                try {
                    processedRequest = this.checkMultipart(request);
                    multipartRequestParsed = processedRequest != request;
                    mappedHandler = this.getHandler(processedRequest);
                    if (mappedHandler == null) {
                        this.noHandlerFound(processedRequest, response);
                        return;
                    }

                    HandlerAdapter ha = this.getHandlerAdapter(mappedHandler.getHandler());
                    String method = request.getMethod();
                    boolean isGet = "GET".equals(method);
                    if (isGet || "HEAD".equals(method)) {
                        long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Last-Modified value for [" + getRequestUri(request) + "] is: " + lastModified);
                        }

                        if ((new ServletWebRequest(request, response)).checkNotModified(lastModified) && isGet) {
                            return;
                        }
                    }

                    if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                        return;
                    }

                    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
                    if (asyncManager.isConcurrentHandlingStarted()) {
                        return;
                    }

                    this.applyDefaultViewName(processedRequest, mv);
                    mappedHandler.applyPostHandle(processedRequest, response, mv);
                } catch (Exception var20) {
                    dispatchException = var20;
                } catch (Throwable var21) {
                    dispatchException = new NestedServletException("Handler dispatch failed", var21);
                }

                this.processDispatchResult(processedRequest, response, mappedHandler, mv, (Exception)dispatchException);
            } catch (Exception var22) {
                this.triggerAfterCompletion(processedRequest, response, mappedHandler, var22);
            } catch (Throwable var23) {
                this.triggerAfterCompletion(processedRequest, response, mappedHandler, new NestedServletException("Handler processing failed", var23));
            }

        } finally {
            if (asyncManager.isConcurrentHandlingStarted()) {
                if (mappedHandler != null) {
                    mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
                }
            } else if (multipartRequestParsed) {
                this.cleanupMultipart(processedRequest);
            }

        }
    }
```



#### Thymeleaf内置变量

1. #strings 	org.thymeleaf.expression.Strings

对应关系

```xml
	<!-- Thymeleaf Expression Objects -->
	<!-- ============================ -->

	<expression-object name="ctx"                class="org.thymeleaf.context.IContext"/>
	<expression-object name="root"               class="org.thymeleaf.context.IContext"/>
	<expression-object name="vars"               class="org.thymeleaf.context.IContext"/>
	<expression-object name="object"             class="org.thymeleaf.context.IContext"/>
	<expression-object name="locale"             class="java.util.Locale"/>

	<expression-object name="request"            class="javax.servlet.http.HttpServletRequest"/>
	<expression-object name="response"           class="javax.servlet.http.HttpServletResponse"/>
	<expression-object name="session"            class="javax.servlet.http.HttpSession"/>
	<expression-object name="servletContext"     class="javax.servlet.ServletContext"/>

	<expression-object name="httpServletRequest" class="javax.servlet.http.HttpServletRequest"/>
	<expression-object name="httpSession"        class="javax.servlet.http.HttpSession"/>

	<expression-object name="conversions" class="org.thymeleaf.expression.Conversions"/>
	<expression-object name="uris"        class="org.thymeleaf.expression.Uris"/>

	<expression-object name="calendars"  class="org.thymeleaf.expression.Calendars"/>
	<expression-object name="dates"      class="org.thymeleaf.expression.Dates"/>
	<expression-object name="bools"      class="org.thymeleaf.expression.Bools"/>
	<expression-object name="numbers"    class="org.thymeleaf.expression.Numbers"/>
	<expression-object name="objects"    class="org.thymeleaf.expression.Objects"/>
	<expression-object name="strings"    class="org.thymeleaf.expression.Strings"/>
	<expression-object name="arrays"     class="org.thymeleaf.expression.Arrays"/>
	<expression-object name="lists"      class="org.thymeleaf.expression.Lists"/>
	<expression-object name="sets"       class="org.thymeleaf.expression.Sets"/>
	<expression-object name="maps"       class="org.thymeleaf.expression.Maps"/>
	<expression-object name="aggregates" class="org.thymeleaf.expression.Aggregates"/>
	<expression-object name="messages"   class="org.thymeleaf.expression.Messages"/>
	<expression-object name="ids"        class="org.thymeleaf.expression.Ids"/>

	<expression-object name="execInfo" class="org.thymeleaf.expression.ExecutionInfo"/>
```

`StandardExpressionObjectFactory`  -> 构建 `IExpressionContext`

```java
public class StandardExpressionObjectFactory implements IExpressionObjectFactory {
    public static final String CONTEXT_EXPRESSION_OBJECT_NAME = "ctx";
    public static final String ROOT_EXPRESSION_OBJECT_NAME = "root";
    public static final String VARIABLES_EXPRESSION_OBJECT_NAME = "vars";
    public static final String SELECTION_TARGET_EXPRESSION_OBJECT_NAME = "object";
    public static final String LOCALE_EXPRESSION_OBJECT_NAME = "locale";
    public static final String REQUEST_EXPRESSION_OBJECT_NAME = "request";
    public static final String RESPONSE_EXPRESSION_OBJECT_NAME = "response";
    public static final String SESSION_EXPRESSION_OBJECT_NAME = "session";
    public static final String SERVLET_CONTEXT_EXPRESSION_OBJECT_NAME = "servletContext";
    public static final String CONVERSIONS_EXPRESSION_OBJECT_NAME = "conversions";
    public static final String URIS_EXPRESSION_OBJECT_NAME = "uris";
    public static final String CALENDARS_EXPRESSION_OBJECT_NAME = "calendars";
    public static final String DATES_EXPRESSION_OBJECT_NAME = "dates";
    public static final String BOOLS_EXPRESSION_OBJECT_NAME = "bools";
    public static final String NUMBERS_EXPRESSION_OBJECT_NAME = "numbers";
    public static final String OBJECTS_EXPRESSION_OBJECT_NAME = "objects";
    public static final String STRINGS_EXPRESSION_OBJECT_NAME = "strings";
    public static final String ARRAYS_EXPRESSION_OBJECT_NAME = "arrays";
    public static final String LISTS_EXPRESSION_OBJECT_NAME = "lists";
    public static final String SETS_EXPRESSION_OBJECT_NAME = "sets";
    public static final String MAPS_EXPRESSION_OBJECT_NAME = "maps";
    public static final String AGGREGATES_EXPRESSION_OBJECT_NAME = "aggregates";
    public static final String MESSAGES_EXPRESSION_OBJECT_NAME = "messages";
    public static final String IDS_EXPRESSION_OBJECT_NAME = "ids";
    public static final String EXECUTION_INFO_OBJECT_NAME = "execInfo";
    public static final String HTTP_SERVLET_REQUEST_EXPRESSION_OBJECT_NAME = "httpServletRequest";
    public static final String HTTP_SESSION_EXPRESSION_OBJECT_NAME = "httpSession";
    protected static final Set<String> ALL_EXPRESSION_OBJECT_NAMES = Collections.unmodifiableSet(new LinkedHashSet(Arrays.asList("ctx", "root", "vars", "object", "locale", "request", "response", "session", "servletContext", "conversions", "uris", "calendars", "dates", "bools", "numbers", "objects", "strings", "arrays", "lists", "sets", "maps", "aggregates", "messages", "ids", "execInfo", "httpServletRequest", "httpSession")));
    private static final Uris URIS_EXPRESSION_OBJECT = new Uris();
    private static final Bools BOOLS_EXPRESSION_OBJECT = new Bools();
    private static final Objects OBJECTS_EXPRESSION_OBJECT = new Objects();
    private static final org.thymeleaf.expression.Arrays ARRAYS_EXPRESSION_OBJECT = new org.thymeleaf.expression.Arrays();
    private static final Lists LISTS_EXPRESSION_OBJECT = new Lists();
    private static final Sets SETS_EXPRESSION_OBJECT = new Sets();
    private static final Maps MAPS_EXPRESSION_OBJECT = new Maps();
    private static final Aggregates AGGREGATES_EXPRESSION_OBJECT = new Aggregates();

    public StandardExpressionObjectFactory() {
    }

    public Set<String> getAllExpressionObjectNames() {
        return ALL_EXPRESSION_OBJECT_NAMES;
    }

    public boolean isCacheable(String expressionObjectName) {
        return expressionObjectName != null && !expressionObjectName.equals("object");
    }

    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if ("object".equals(expressionObjectName)) {
            if (context instanceof ITemplateContext) {
                ITemplateContext templateContext = (ITemplateContext)context;
                if (templateContext.hasSelectionTarget()) {
                    return templateContext.getSelectionTarget();
                }
            }

            return context;
        } else if ("root".equals(expressionObjectName)) {
            return context;
        } else if ("vars".equals(expressionObjectName)) {
            return context;
        } else if ("ctx".equals(expressionObjectName)) {
            return context;
        } else if ("locale".equals(expressionObjectName)) {
            return context.getLocale();
        } else if ("request".equals(expressionObjectName)) {
            return context instanceof IWebContext ? ((IWebContext)context).getRequest() : null;
        } else if ("response".equals(expressionObjectName)) {
            return context instanceof IWebContext ? ((IWebContext)context).getResponse() : null;
        } else if ("session".equals(expressionObjectName)) {
            return context instanceof IWebContext ? ((IWebContext)context).getSession() : null;
        } else if ("servletContext".equals(expressionObjectName)) {
            return context instanceof IWebContext ? ((IWebContext)context).getServletContext() : null;
        } else if ("httpServletRequest".equals(expressionObjectName)) {
            return context instanceof IWebContext ? ((IWebContext)context).getRequest() : null;
        } else if ("httpSession".equals(expressionObjectName)) {
            return context instanceof IWebContext ? ((IWebContext)context).getSession() : null;
        } else if ("conversions".equals(expressionObjectName)) {
            return new Conversions(context);
        } else if ("uris".equals(expressionObjectName)) {
            return URIS_EXPRESSION_OBJECT;
        } else if ("calendars".equals(expressionObjectName)) {
            return new Calendars(context.getLocale());
        } else if ("dates".equals(expressionObjectName)) {
            return new Dates(context.getLocale());
        } else if ("bools".equals(expressionObjectName)) {
            return BOOLS_EXPRESSION_OBJECT;
        } else if ("numbers".equals(expressionObjectName)) {
            return new Numbers(context.getLocale());
        } else if ("objects".equals(expressionObjectName)) {
            return OBJECTS_EXPRESSION_OBJECT;
        } else if ("strings".equals(expressionObjectName)) {
            return new Strings(context.getLocale());
        } else if ("arrays".equals(expressionObjectName)) {
            return ARRAYS_EXPRESSION_OBJECT;
        } else if ("lists".equals(expressionObjectName)) {
            return LISTS_EXPRESSION_OBJECT;
        } else if ("sets".equals(expressionObjectName)) {
            return SETS_EXPRESSION_OBJECT;
        } else if ("maps".equals(expressionObjectName)) {
            return MAPS_EXPRESSION_OBJECT;
        } else if ("aggregates".equals(expressionObjectName)) {
            return AGGREGATES_EXPRESSION_OBJECT;
        } else if ("messages".equals(expressionObjectName)) {
            return context instanceof ITemplateContext ? new Messages((ITemplateContext)context) : null;
        } else if ("ids".equals(expressionObjectName)) {
            return context instanceof ITemplateContext ? new Ids((ITemplateContext)context) : null;
        } else if ("execInfo".equals(expressionObjectName)) {
            return context instanceof ITemplateContext ? new ExecutionInfo((ITemplateContext)context) : null;
        } else {
            return null;
        }
    }
}

```





#### 模板寻址

​	prefix + viewName + suffix

#### 模板缓存

​	一般 cache = true

​	当cache = false的时候，可以设置缓存时间

#### 学习技巧

​	假设学习Thymeleaf -》 Thymeleaf Properties -》 ThymeleafProperties

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {
	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
	public static final String DEFAULT_PREFIX = "classpath:/templates/";
	public static final String DEFAULT_SUFFIX = ".html";
	private boolean checkTemplate = true;
	private boolean checkTemplateLocation = true;
	private String prefix = DEFAULT_PREFIX;
	private String suffix = DEFAULT_SUFFIX;
	private String mode = "HTML";
	private Charset encoding = DEFAULT_ENCODING;
	private boolean cache = true;
	private Integer templateResolverOrder;
	private String[] viewNames;
	private String[] excludedViewNames;
	private boolean enableSpringElCompiler;
	private boolean enabled = true;

	public static class Servlet {
		private MimeType contentType = MimeType.valueOf("text/html");
		public MimeType getContentType() {
			return this.contentType;
		}
		public void setContentType(MimeType contentType) {
			this.contentType = contentType;
		}
	}
	public static class Reactive {
		private int maxChunkSize;
		private List<MediaType> mediaTypes;
		private String[] fullModeViewNames;
		private String[] chunkedModeViewNames;
	}
}

```

1. 第一步：找到`@ConfigurationProperties(prefix = "spring.thymeleaf")` 确定前缀
2. 第二步：进一步确认字段和属性名是否一一对应



#### Spring MVC 国际化

​	`MessageSourceProperties`

​	注意ViewResolver的接口	

​	LocaleContextHolder

- 来自于DispatcherServlet

- ThreadLocal	

- Locale

  - Servlet	
    - ServletRequest=getLocale()
      - Accept-Language
  - LocalContextHolder
    - 来自于
      - DispatcherServlet
        - FrameworkServlet.initContextHolders
      - 存储是ThreadLocal

  ##### Spring Boot

  - ​	MessageSource

  - ​		MessageSourceAutoConfiguration

  - ​			MessageSourceProperties
    - ​			message.properties
      - ​		message_en.properties
        - ​	message_zh.properties
          - message_zh_CN.properties
          - message_zh_TW..properties

  ​	Thymeleaf

  ​		#key