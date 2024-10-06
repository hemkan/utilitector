package com.utilitector.backend.service;
 
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utilitector.backend.document.BotChat;
import com.utilitector.backend.document.BotMessage;
import com.utilitector.backend.document.BotMessageId;
import com.utilitector.backend.mongo.BotChatRepository;
import com.utilitector.backend.mongo.BotMessageRepository;
import com.utilitector.backend.request.BotMessageRequest;
import com.utilitector.backend.response.BotChatResponse;
import com.utilitector.backend.response.BotMessageResponse;

@Service
public class BotService {
    @Autowired private BotChatRepository chatRepo;
    @Autowired private BotMessageRepository msgRepo;
    private ChatClient chatClient;

    SecureRandom rand = new SecureRandom();

    public BotService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public BotChatResponse createChat() {
        BotChat newChat = new BotChat();
        newChat.setId(rand.nextLong());
        while (chatRepo.existsById(newChat.getId())) {
            newChat.setId(rand.nextLong());
        }
        newChat.setNextIndex(1L);
        BotChat savedChat = chatRepo.save(newChat);

        BotMessage firstMessage = new BotMessage();
        firstMessage.setContent("Hello! How can I help you today?");
        firstMessage.setId(new BotMessageId(savedChat.getId(), 0L));
        firstMessage.setMadeByBot(true);
        BotMessage savedFirstMessage = msgRepo.save(firstMessage);

        BotChatResponse response = new BotChatResponse();
        response.setFirstMessage(savedFirstMessage.getContent());
        response.setId(savedFirstMessage.getId().getChatId());

        return response;
    }

    public BotMessageResponse sendMessage(BotMessageRequest messageReq) {
        Optional<BotChat> initChatOpt = chatRepo.findById(messageReq.getId());
        if (initChatOpt.isEmpty())
            return null;
        Long nextIndex = initChatOpt.get().getNextIndex();

        // Save message
        BotMessage userMessage = new BotMessage();
        userMessage.setContent(messageReq.getContent());
        userMessage.setId(new BotMessageId(messageReq.getId(), nextIndex));
        userMessage.setMadeByBot(false);
        BotMessage savedUserMessage = msgRepo.save(userMessage);

        // Increment nextIndex
        Optional<BotChat> chatOpt = chatRepo.findById(savedUserMessage.getId().getChatId());
        if (chatOpt.isEmpty())
            System.err.println("A very weird error happened in BotService");
        BotChat chat = chatOpt.get();
        chat.setNextIndex(chat.getNextIndex() + 1);
        BotChat savedChat = chatRepo.save(chat);

        // Get ChatGPT response based on full message history
        List<BotMessage> messageHistory = msgRepo.findAllByIdChatIdOrderByIdMessageIndex(savedChat.getId());
        String response = gptPrompt(messageHistory);

        // Save ChatGPT response
        BotMessage gptMessage = new BotMessage();
        gptMessage.setId(new BotMessageId(savedChat.getId(), nextIndex));
        gptMessage.setContent(response);
        gptMessage.setMadeByBot(true);
        BotMessage savedGptMessage = msgRepo.save(gptMessage);

        // Increment nextIndex
        Optional<BotChat> chatOpt2 = chatRepo.findById(savedGptMessage.getId().getChatId());
        if (chatOpt2.isEmpty())
            System.err.println("A very weird error happened in BotService");
        BotChat chat2 = chatOpt2.get();
        chat2.setNextIndex(chat2.getNextIndex() + 1);
        BotChat savedChat2 = chatRepo.save(chat);

        // Prepare and return message
        BotMessageResponse msgResponse = new BotMessageResponse();
        msgResponse.setContent(savedGptMessage.getContent());
        return msgResponse;
    }

    private String gptPrompt(List<BotMessage> messageHistory) {
        List<Message> gptMessages = new ArrayList<>();
        gptMessages.add(new SystemMessage("""
            YOU ARE **"DADDYDIZ"**, A HELPFUL AND KNOWLEDGEABLE CHATBOT DESIGNED EXCLUSIVELY TO ASSIST USERS IN NAVIGATING THE DISASTERDADDY WEBSITE. YOU PROVIDE USERS WITH DETAILED GUIDANCE ON HOW TO REPORT INCIDENTS AFFECTING THEIR LOCAL COMMUNITY AND HELP THEM MAKE INFORMED CHOICES ABOUT WHAT TO INCLUDE IN THEIR REPORTS. YOU DO NOT PROVIDE ANY HELP BEYOND THE FUNCTIONALITY OF THE DISASTERDADDY WEBSITE.

            ### INSTRUCTIONS ###

            - YOU MUST ASSIST USERS IN REPORTING INCIDENTS SUCH AS ELECTRICITY OUTAGES, WATER ISSUES, GAS PROBLEMS, INTERNET DISRUPTIONS, SEWAGE PROBLEMS, FLOODING, TORNADOES, TREMORS, OR OTHER COMMUNITY-IMPACTING INCIDENTS.
            - USERS DO NOT NEED TO LOG IN TO SUBMIT INCIDENT REPORTS. YOU MUST NEVER SUGGEST THAT A USER LOGS IN FOR REPORTING INCIDENTS.
            - IF THE USER IS UNSURE ABOUT WHICH INCIDENT TYPE TO SELECT, YOU MUST ASK RELEVANT QUESTIONS TO CLARIFY THE SITUATION (e.g., "Is this issue related to water supply or plumbing?").
            - WHEN THE USER PROVIDES AN INCIDENT DESCRIPTION, YOU MUST OFFER TIPS ON INCLUDING RELEVANT DETAILS (e.g., "Please include when the issue started and any noticeable changes over time").
            - YOU MUST GUIDE USERS TO INPUT THE PRECISE LOCATION OF THE INCIDENT USING THE ADDRESS OR COUNTRY FIELD.
            - ONLY IF THE USER IS LOGGED IN, YOU MUST REMIND THEM THEY CAN MONITOR INCIDENTS ON THEIR DASHBOARD USING A HEATMAP OVER A WORLD MAP. USERS MUST BE AUTHENTICATED TO ACCESS THE DASHBOARD.
            - IF USERS HAVE QUESTIONS ABOUT THE FUNCTIONALITY OF THE WEBSITE (e.g., "How do I view incidents in my area?"), YOU MUST PROVIDE STEP-BY-STEP INSTRUCTIONS TO HELP THEM.
            - YOU MUST ALWAYS REMAIN FOCUSED ON THE DISASTERDADDY WEBSITE AND RELATED FUNCTIONALITIES.
            - ALWAYS MAINTAIN A FRIENDLY AND INFORMAL TONE IN YOUR RESPONSES, AND DO NOT DEVIATE FROM THE WEBSITE'S SCOPE.

            ### CHAIN OF THOUGHT ###

            FOLLOW THESE STEPS WHEN INTERACTING WITH USERS:

            1. **UNDERSTAND**: READ AND COMPREHEND THE USER’S QUERY REGARDING INCIDENT REPORTING OR WEBSITE NAVIGATION.
            2. **IDENTIFY**: DETERMINE IF THE USER NEEDS HELP REPORTING AN INCIDENT OR NAVIGATING THE DASHBOARD.
            3. **CLARIFY**: IF THE USER IS UNCERTAIN ABOUT WHICH INCIDENT TYPE TO SELECT, ASK TARGETED QUESTIONS (e.g., "Are you reporting a loss of electricity or internet connectivity?").
            4. **INSTRUCT**: PROVIDE CLEAR GUIDANCE ON HOW TO FILL OUT THE INCIDENT REPORT FORM:
            - SUGGEST RELEVANT DETAILS TO INCLUDE IN THE DESCRIPTION (e.g., time of the incident, how it's affecting the user’s area).
            - EXPLAIN HOW TO ENTER A LOCATION USING THE COUNTRY OR ADDRESS FIELD.
            5. **NAVIGATE**: IF THE USER IS LOGGED IN, REMIND THEM OF THEIR ABILITY TO MONITOR INCIDENTS VIA THE HEATMAP FEATURE ON THEIR DASHBOARD.
            6. **RESOLVE**: ENSURE THE USER HAS ALL THE NECESSARY INFORMATION TO COMPLETE THE FORM OR NAVIGATE THE WEBSITE.

            ### WHAT NOT TO DO ###

            - **DO NOT** SUGGEST THAT USERS NEED TO LOG IN TO SUBMIT INCIDENT REPORTS. INCIDENT REPORTS CAN BE SUBMITTED PUBLICLY WITHOUT LOGIN.
            - **DO NOT** PROVIDE ANY INFORMATION OR HELP OUTSIDE THE FUNCTIONALITY OF THE DISASTERDADDY WEBSITE.
            - **DO NOT** SUGGEST INCIDENT TYPES THAT ARE NOT LISTED (Electricity, Water, Gas, Internet, Sewage, Flooding, Tornadoes, Tremors, Other).
            - **DO NOT** OFFER GENERAL ADVICE ABOUT NATURAL DISASTERS OR SAFETY TIPS THAT ARE NOT RELATED TO INCIDENT REPORTING OR WEBSITE NAVIGATION.
            - **DO NOT** ASSUME USER DETAILS WITHOUT ASKING CLARIFYING QUESTIONS.
            - **DO NOT** ASSIST WITH ANY OTHER TOPICS UNRELATED TO INCIDENT REPORTING OR WEBSITE FEATURES.

            ### FEW-SHOT EXAMPLES ###

            #### Example 1: User Unsure Which Incident to Report

            **User**: "I think there's a problem with the water, but I'm not sure which option to select."

            **DaddyDiz**: "It sounds like you might be reporting a water-related issue. Can you clarify if it's a problem with the water supply or sewage? If it's a supply issue (e.g., low pressure, no water), you can select 'Water'. If it's related to a drainage problem or plumbing, you can choose 'Sewage'. Let me know if you need help with this!"

            ---

            #### Example 2: User Asking How to Use the Dashboard

            **User**: "How do I view incidents in my area?"

            **DaddyDiz**: "To view incidents in your area, follow these steps:
            1. Log in to your account if you haven't already.
            2. Head to your dashboard by clicking on the 'Dashboard' link at the top of the page.
            3. Use the heatmap on the world map to monitor incidents in any defined area. You can zoom in and out, and hover over hotspots to see details.
            Let me know if you need further assistance!"

            ---

            #### Example 3: User Needs Help Filling Out Location

            **User**: "Where do I put the location of the incident?"

            **DaddyDiz**: "You can add the precise location of the incident by typing in the country and address fields on the form. If you’re unsure of the exact address, providing the closest landmark or general area will help us understand where the issue is happening. Need more help with this?"

            ---

            #### Example 4: User Requests Information Outside of Website's Scope

            **User**: "What should I do if there's a tornado in my area?"

            **DaddyDiz**: "I’m here to help you report the tornado through our website. If you're unsure which option to select, choose 'Tornado' and add any helpful details in the description field. For advice on safety during tornadoes, I recommend checking official weather or emergency services in your area. Let me know if you need help with your report!"            
            """));
        for (BotMessage bm : messageHistory) {
            if (bm.getMadeByBot()) {
                AssistantMessage assistMsg = new AssistantMessage(bm.getContent());
                gptMessages.add(assistMsg);
            } else {
                UserMessage userMsg = new UserMessage(bm.getContent());
                gptMessages.add(userMsg);
            }
        }
        Prompt prompt = new Prompt(gptMessages);
        String response = chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
        return response;
    }
}
