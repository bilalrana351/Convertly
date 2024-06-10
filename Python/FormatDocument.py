import openai
from openai import OpenAI
import os
import json
import Constants
import docx
import htmldocx

# This will be used to get the openai api key
openai.api_key = os.getenv("OPENAI_API_KEY")
client = OpenAI()

# Function to call GPT-3/GPT-4  
def getGPTResponse(systemPrompt, userInput):
    try:
        completion = client.chat.completions.create(
        model = "gpt-3.5-turbo",
        response_format = {"type" : "json_object"},
        messages = [
            {"role": "assistant", "content": systemPrompt},
            {"role": "user", "content": userInput}
        ],
        tools = getGPTFunctions(),
        tool_choice = {"type": "function", "function": {"name": "get_formatted_response"}}
        )

        # This is the actual response
        actualResponse = completion.choices[0].message.tool_calls[0].function.arguments

        # Lets convert it into the json format
        return json.loads(actualResponse)
        
    except Exception as e:
        # These error types are still in prototype, we will have to check with the frontend to determine what type of errors they want
        raise RuntimeError("Error in GPT call : " + str(e))

# This will be what will be used to get the gpt functions
def getGPTFunctions():
    structuredFunctions = [
        {
            "type" : "function",
            "function":{
                "name" : "get_formatted_response",
                "desription" : f"{Constants.FUNCTION_PROMPT}",
                "parameters": {
                    "type" : "object",
                    "properties" : {
                        "formattedDocumentHTML" : {
                            "description" : "This will be the html of the formatted document that will be returned to the user."
                        }
                    }
                }
            }
        }
    ]

    return structuredFunctions

# This will be the function that will be used to get the response from the GPT Model
def getResponse(textInput):
    # First of all we wil call the getGPTResponse function
    response = getGPTResponse(Constants.SYSTEM_PROMPT, textInput)

    # Now we will return the formatted document
    return {"formattedDocument" : response["formattedDocumentHTML"]}

if __name__ == "__main__":
    raise RuntimeError("This file is not meant to be run directly")