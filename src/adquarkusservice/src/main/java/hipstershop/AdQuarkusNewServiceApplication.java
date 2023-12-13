// AdQuarkusServiceApplication.java
import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.configuration.ProfileManager;

@QuarkusMain
public class AdQuarkusServiceApplication implements QuarkusApplication {

    @GrpcService
    AdQuarkusServiceImpl service;

    public static void main(String... args) {
        Quarkus.run(AdQuarkusServiceApplication.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        System.out.println("AdQuarkusService is running on port 9000");
        Quarkus.waitForExit();
        return 0;
    }
}
