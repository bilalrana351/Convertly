# This will be the main node of the communication between the java application and our project, it will work as follows
from flask import Flask, request, redirect, url_for, render_template, send_file, jsonify,request
import AccessAzureApi
import FormatDocument
from docx import Document
from htmldocx import HtmlToDocx
import io

app = Flask(__name__)

# This will be the first path that will be called by the java application to send all the pictures that have been taken by the user
@app.route('/sendImages', methods=['POST'])
def sendImagesAndGetResponse():
    # First of all, we will extract the paths of the incoming images from the request by the java application
    # This will be the json object for the response generated
    responseJson = request.get_json()

    if responseJson is None:
        return jsonify({"error": "No json object found"})
    
    # Now we will extract the paths of the images from the json object
    paths = responseJson['imagePaths']

    for path in paths:
        print(path) 

    # This will be the variable that will keep track of the raw response
    rawResponse = ""

    # Now we will call the extractTextFromImage function to get the text from the images
    for path in paths:
        rawResponse += (AccessAzureApi.extractTextFromImage(path) + "\n")
    
    # We will now call the gpt function to get the formatted response
    try:
        formattedResponse = FormatDocument.getResponse(rawResponse)["formattedDocument"]

        # Now we will get the formatted html response into the word file using the docx module of python and the htmldocx module of python
        document = Document()

        # Now we will convert the html to docx
        htmlParser = HtmlToDocx()

        htmlParser.add_html_to_document(formattedResponse, document)

        # Save the document to an in-memory buffer
        buffer = io.BytesIO()
        document.save(buffer)
        buffer.seek(0)

        return send_file(buffer, as_attachment=True, download_name='output.docx',
                         mimetype='application/vnd.openxmlformats-officedocument.wordprocessingml.document')

    except Exception as e:
        return jsonify({"error": str(e)})

# This will be the route that will shut down the flask server
@app.route('/shutdown', methods=['GET'])
def shutdown():
    func = request.environ.get('werkzeug.server.shutdown')
    if func is None:
        raise RuntimeError('Not running with the Werkzeug Server')
    func()
    return jsonify({"message": "Server shutting down"})

# This will start the flask server
app.run(port=5000,host = "127.0.0.1", debug=True)