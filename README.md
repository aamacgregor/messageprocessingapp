# messageprocessingapp
Message Processing Application

My aim was to get an application running that met the functional requirements. I would have liked to have done a bit of TDD on this task. But, instead, I had the wild idea to put it in a Spring Boot app... which used up time that I could have spent on TDD instead. I imported two dependencies in the dependency section, but they import more... a lot more, so in effect, I would say I failed to two dependency limit.

Hindsight is a wonderful thing. If I were to do the test again, I would not have put the code within a Spring boot app, but instead, utilised TDD and only submitted the core logic of the application. Still, the application is fulling functional, so if you feel like running it, then you can. If you do, then, here are some example messages

```javascript
//MessageType1 endpoint /api/sale
{
  "product" : "Apple",
  "value" : 2
}

//MessageType2 endpoint /api/sale
{
  "product" : "Apple",
  "value" : 2,
  "quantity" : 5
}

//MessageType2 endpoint /api/adjustment the adjustmentOperation values are ADD, SUBTRACT, MULTIPLY
{
  "product" : "Apple",
  "adjustment" : "1.5",
  "adjustmentOperation" : "MULTIPLY"
}
```

There is one major integration test, which is a test to rule them all, this is not how I would do testing (I did it only because I'd run out of time and something is better than nothing). Single responsibilities applies to testing too, so, each test should be testing something specific. There are also a couple of unit tests that are normal tests. Basically, the testing is lacking, and I would never usually check in code that is so poorly tested. That said, the test to rule them all gets a reasonable coverage. Again, if I had more time, I would have done some proper testing.
