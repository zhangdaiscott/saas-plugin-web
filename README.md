插件式开发框架，适合企业SAAS应用开发，方便扩展第三方插件，插件以JAR形式存在
===============
saas-plugin-web
===============

Saas, Component, Module, Plugin


    1.P3-Core MVC架构采用coc命名规范来实现请求触发
    2.项目默认访问主页
      http://localhost:8888
    3.action触发方法
      http://localhost:8888/P3-example/demo/demo.action  
      com.buss.demo.action.DemoAction
      action所在目录/Action去掉(Action)追加后缀.action
        a.默认action访问的是execute方法
        b.demo!demo.action
      
    4.页面层面不能采用jsp，需要采用模板语言freemarker和velicity
    5.实现插件式开发，按照模块进行开发，每个模块可以单独达成jar包
