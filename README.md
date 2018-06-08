# forex-paidy-test

## Usage (SBT)

- Build and run tests
```bash
sbt clean compile test
``` 

- Running server (SBT) : 
```bash
export ONEFORGE_API_KEY="<your-oneforge-api-key>"
sbt run
```

## Usage (Docker)
- Build docker image : 
```bash
sbt docker:publishLocal
```

- Run container : 
```bash
docker run --rm -it -e ONEFORGE_API_KEY=<your-oneforge-api-key> --network host forex-proxy:1.0.0
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
- [x] Docker packaging
- [x] Add CI

### Notes

