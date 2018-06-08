# forex-paidy-test

## Usage

- Running server : 
```bash
export ONEFORGE_API_KEY="<your-oneforge-api-key>"
sbt run
```

- Running tests
```bash
sbt test
``` 

### Endpoints : 
-  Get one rate 
```bash
curl http://localhost:8888/?from=EUR&to=JPY
```

- Get all supported rates 
```bash
curl http://localhost:8888/all
```

### TODO 
- [x] OneForge client live interpreter   
- [x] Add configuration for oneForge client   
- [ ] Cache with Memoized monad ?  
- [ ] Optimize to fit in free tier plan of 1000 r/day  
- [ ] Support circuit breaker for OneForge API  
- [ ] Add metrics on API calls (Writer monad ?)  
- [ ] Add tests  
- [ ] Add benchmark  
- [ ] Create Dockerfile


