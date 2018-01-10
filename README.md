# neural-networks
MNIST image classifier and easy-to-use Java ANN library, version 1.1. Create, save, load, and train neural networks.

# Current Features #
* Creating networks given layer size parameters
```java
Network net = new Network(784,100,10);
```
* Loading networks from text files
```java
Network.loadNetworkFromResource("foo.txt");
```
* Saving networks at text files
```java
net.saveNetworkAtResource("foo.txt");
```
* Training networks from MNIST dataset
```java
net.trainFromMnistData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte", eta, start, end, epochs, bw);
```
* Testing networks from MNIST dataset
```java
net.testAll("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte", start, end, showMistakes, bw);
```
* Network at network.txt that can classify MNIST images
* Image prompt, applet, and classifier to showcase library

![Screenshot](/NeuralNets/Screen%20Shot%202018-01-07%20at%207.10.39%20PM.png)

# Future Plans #
* Implement other types of neural nets
* Add support for other datasets/applications
* GUI to facilitate usage of library
* Optimize and implement more efficient algorithms

# Author and Licensing Information #
Written by Reedit Shahriar in January 2018. Licensed under MIT License.

# Notes #
* Used code from [this repo](https://github.com/turkdogan/mnist-data-reader) to open MNIST images and read data.
