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
Environment variable : 
- `ONEFORGE_API_KEY` : used to provide OneForge API key

### Endpoints : 
-  Get one rate 
```bash
curl http://localhost:8888/?from=EUR&to=JPY
```

- Get all supported rates 
```bash
curl http://localhost:8888/all
```

### Notes
- Requests to OneForge external service use http4s client. 
First dropped implementation used akka HTTP client but I preferred to use http4s for IO instead of Akka Futures.

- To not exceed OneForge API free-tier limitation of 1000 requests per day, all supported currency pair are 
requested then cached using taskMemoized backed with Caffeine cache adjusted to 2min retention. 
That way, we ensure that we will used at most 720 of 1000 requests allowed on free-tier plan and returned rates 
will be not older that 5 minutes. (24 * 60) / 2 = 720. This cache retention could be adjusted through configuration
but should not be less than 90s.

- Application is package with Docker (using sbt-native-packager plugin) assuming that it will run on Docker environment.

- Circe dependency (along others) was updated to remove backport of deriveUnwrappedDecoder/Encoder from circe 0.9.0

- Some unit test were written but without full coverage
    
### Production ready TODO list :  
- [x] OneForge client live interpreter   
- [x] Add configuration for oneForge client   
- [x] Cache with Memoized monad 
- [x] Optimize to fit in free tier plan of 1000 r/day  
- [ ] Add metrics on API calls (Writer monad ?)  
- [ ] Add API tests  
- [ ] Add benchmark  
- [x] Docker packaging
- [x] Add CI

