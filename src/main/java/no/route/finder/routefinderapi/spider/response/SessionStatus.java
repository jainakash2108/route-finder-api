package no.route.finder.routefinderapi.spider.response;

public record SessionStatus(String id, Boolean isReady, String setupProgress, String errorDuringSetup,
                            Boolean optimizationIsRunning, Integer iterationCount, String optimizationTime,
                            Double bestSolutionValue, String internalOptimizerError) {
    public SessionStatus(String sessionId) {
        this(
                sessionId,
                false,
                null,
                null,
                false,
                0,
                null,
                0d,
                null
        );
    }
}