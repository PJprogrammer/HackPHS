/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.example.android.hackPHS;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Base64;
import java.io.ByteArrayOutputStream;


public class ImageClassifier {


  static final int DIM_IMG_SIZE_X = 224;
  static final int DIM_IMG_SIZE_Y = 224;
  private TextRecognizer textRecognizer;
  private Socket socket;
  private DataOutputStream writer;

  ImageClassifier(Activity activity) throws IOException {
    textRecognizer = new TextRecognizer.Builder(activity.getApplicationContext()).build();
    Log.i("hello","Yes");
  }

  public String classifyFrame(Bitmap bitmap) {

      StringBuilder sb = new StringBuilder();

      if(!textRecognizer.isOperational())
          Log.i("TEXTSTATUS","not working");
      else {
          Frame frame = new Frame.Builder().setBitmap(bitmap).build();
          SparseArray<TextBlock> items = textRecognizer.detect(frame);

          for (int i = 0; i < items.size(); i++) {
              TextBlock myItem = items.valueAt(i);
              sb.append(myItem.getValue());
              sb.append("\n");
          }

      }

      return sb.toString();
  }

  public void sendImage(Bitmap bitmap) throws IOException{
      try {
          if(socket == null) {
              socket = new Socket("192.168.137.1",55555);
              writer = new DataOutputStream(socket.getOutputStream());
          }

          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

          writer.writeUTF("#");
          writer.writeUTF(Integer.toString(outputStream.toByteArray().length));
          writer.writeUTF("#");
          writer.write(outputStream.toByteArray());
          writer.flush();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  public void close() {
/*

import socket
import base64
import cv2
import sys

print("hello")

address = ('192.168.137.1', 55555)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(address)
s.listen(5)
client, addr = s.accept()
print ('got connected from', addr)

c = ""
numLen = ""


while True:
    if client.recv(1).decode("utf-8") == '#':
        while c != "#":
            c = client.recv(1).decode("utf-8")
            numLen += c

        numLen = numLen[:-1]

        img = base64.b64decode(client.recv(int(numLen)).decode("utf-8"))

        c = ""
        numLen = ""
        cv2.imshow('frame',img)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break


cv2.destroyAllWindows()

 */
  }


}
