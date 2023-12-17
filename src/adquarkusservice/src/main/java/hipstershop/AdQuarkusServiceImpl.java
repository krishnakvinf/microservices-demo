/*
 * Copyright 2018, Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hipstershop;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import hipstershop.Demo.AdQuarkus;
import hipstershop.Demo.AdQuarkusRequest;
import hipstershop.Demo.AdQuarkusResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;


  @GrpcService
  public class AdQuarkusServiceImpl implements AdQuarkusService {

    private static final Logger logger = LogManager.getLogger(AdQuarkusServiceImpl.class);

    private static int MAX_ADS_TO_SERVE = 2;

    /**
     * Retrieves ads based on context provided in the request {@code AdQuarkusRequest}.
     *
     * @param req the request containing context.
     * @param responseObserver the stream observer which gets notified with the value of {@code
     *     AdQuarkusResponse}
     */
    @Override
    public Uni<AdQuarkusResponse> getAdsQuarkus(AdQuarkusRequest req) {
      
      AdQuarkusResponse reply = null;
      try {
        List<AdQuarkus> allAds = new ArrayList<>();
        logger.info("received ad request (context_words=" + req.getContextKeysList() + ")");
        if (req.getContextKeysCount() > 0) {
          for (int i = 0; i < req.getContextKeysCount(); i++) {
            Collection<AdQuarkus> ads = this.getAdsByCategory(req.getContextKeys(i));
            allAds.addAll(ads);
          }
        } else {
          allAds = this.getRandomAds();
        }
        if (allAds.isEmpty()) {
          // Serve random ads.
          allAds = this.getRandomAds();
        }
        reply = AdQuarkusResponse.newBuilder().addAllAds(allAds).build();
        // responseObserver.onNext(reply);
        // responseObserver.onCompleted();
        
      } catch (Exception e) {
        logger.log(Level.WARN, "GetAds Failed with status {}", e.getMessage());
        // responseObserver.onError(e);
      }
      return Uni.createFrom().item(reply);
    }

    private static final ImmutableListMultimap<String, AdQuarkus> adsMap = createAdsMap();

    private Collection<AdQuarkus> getAdsByCategory(String category) {
      return adsMap.get(category);
    }

    private static final Random random = new Random();

    private List<AdQuarkus> getRandomAds() {
      List<AdQuarkus> ads = new ArrayList<>(MAX_ADS_TO_SERVE);
      Collection<AdQuarkus> allAds = adsMap.values();
      for (int i = 0; i < MAX_ADS_TO_SERVE; i++) {
        ads.add(Iterables.get(allAds, random.nextInt(allAds.size())));
      }
      return ads;
    }

    private static ImmutableListMultimap<String, AdQuarkus> createAdsMap() {
      AdQuarkus hairdryer =
          AdQuarkus.newBuilder()
              .setRedirectUrl("/product/2ZYFJ3GM2N")
              .setText("Hairdryer for sale. 50% off.")
              .build();
      AdQuarkus tankTop =
          AdQuarkus.newBuilder()
              .setRedirectUrl("/product/66VCHSJNUP")
              .setText("Tank top for sale. 20% off.")
              .build();
      AdQuarkus candleHolder =
          AdQuarkus.newBuilder()
              .setRedirectUrl("/product/0PUK6V6EV0")
              .setText("Candle holder for sale. 30% off.")
              .build();
      AdQuarkus bambooGlassJar =
          AdQuarkus.newBuilder()
              .setRedirectUrl("/product/9SIQT8TOJO")
              .setText("Bamboo glass jar for sale. 10% off.")
              .build();
      AdQuarkus watch =
          AdQuarkus.newBuilder()
              .setRedirectUrl("/product/1YMWWN1N4O")
              .setText("Watch for sale. Buy one, get second kit for free")
              .build();
      AdQuarkus mug =
          AdQuarkus.newBuilder()
              .setRedirectUrl("/product/6E92ZMYYFZ")
              .setText("Mug for sale. Buy two, get third one for free")
              .build();
      AdQuarkus loafers =
          AdQuarkus.newBuilder()
              .setRedirectUrl("/product/L9ECAV7KIM")
              .setText("Loafers for sale. Buy one, get second one for free")
              .build();
      return ImmutableListMultimap.<String, AdQuarkus>builder()
          .putAll("clothing", tankTop)
          .putAll("accessories", watch)
          .putAll("footwear", loafers)
          .putAll("hair", hairdryer)
          .putAll("decor", candleHolder)
          .putAll("kitchen", bambooGlassJar, mug)
          .build();
    }

  }  
