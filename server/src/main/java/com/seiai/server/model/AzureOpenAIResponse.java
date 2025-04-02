package com.seiai.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AzureOpenAIResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private String system_fingerprint;
    private List<Choice> choices;
    private List<PromptFilterResult> prompt_filter_results;
    private Usage usage;

    @Data
    public static class Choice {
        private Message message;
        private int index;
        private String finish_reason;
        private Object logprobs;
        private ContentFilterResults content_filter_results;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
        private Object refusal;
    }

    @Data
    public static class ContentFilterResults {
        private FilterResult hate;
        private FilterResult self_harm;
        private FilterResult sexual;
        private FilterResult violence;
    }

    @Data
    public static class FilterResult {
        private boolean filtered;
        private String severity;
    }

    @Data
    public static class PromptFilterResult {
        @JsonProperty("prompt_index")
        private int promptIndex;
        private ContentFilterResults content_filter_results;
        private JailbreakFilter jailbreak;
    }

    @Data
    public static class JailbreakFilter {
        private boolean filtered;
        private boolean detected;
    }

    @Data
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
        @JsonProperty("prompt_tokens_details")
        private TokenDetails promptTokensDetails;
        @JsonProperty("completion_tokens_details")
        private CompletionTokenDetails completionTokensDetails;
    }

    @Data
    public static class TokenDetails {
        @JsonProperty("audio_tokens")
        private int audioTokens;
        @JsonProperty("cached_tokens")
        private int cachedTokens;
    }

    @Data
    public static class CompletionTokenDetails {
        @JsonProperty("accepted_prediction_tokens")
        private int acceptedPredictionTokens;
        @JsonProperty("audio_tokens")
        private int audioTokens;
        @JsonProperty("reasoning_tokens")
        private int reasoningTokens;
        @JsonProperty("rejected_prediction_tokens")
        private int rejectedPredictionTokens;
    }
}