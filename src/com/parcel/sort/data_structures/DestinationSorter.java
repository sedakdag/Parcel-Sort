package com.parcel.sort.data_structures;

import com.parcel.sort.entities.Parcel;

public class DestinationSorter {

    public static class ParcelQueue {
        private class Node {
            Parcel parcel;
            Node next;

            public Node(Parcel parcel) {
                this.parcel = parcel;
                this.next = null;
            }
        }

        private Node front, rear;
        private int size;

        public ParcelQueue() {
            front = null;
            rear = null;
            size = 0;
        }

        public void enqueue(Parcel parcel) {
            Node newNode = new Node(parcel);

            // Case 1: Queue is empty or new parcel has higher priority than front
            // (or same priority and arrived earlier)
            if (front == null ||
                    parcel.getPriority() > front.parcel.getPriority() ||
                    (parcel.getPriority() == front.parcel.getPriority() && parcel.getArrivalTick() < front.parcel.getArrivalTick())) {
                newNode.next = front;
                front = newNode;
            } else {
                // Case 2: Traverse to find the correct insertion point
                Node current = front;
                // Continue as long as current.next is not null AND
                // (newParcel has lower priority than next node OR
                //  newParcel has same priority but arrived later than next node)
                while (current.next != null &&
                        (parcel.getPriority() < current.next.parcel.getPriority() ||
                                (parcel.getPriority() == current.next.parcel.getPriority() && parcel.getArrivalTick() > current.next.parcel.getArrivalTick()))) {
                    current = current.next;
                }
                newNode.next = current.next;
                current.next = newNode;
            }
            size++;
        }

        public Parcel dequeue() {
            if (front == null) {
                return null;
            }
            Parcel parcel = front.parcel;
            front = front.next;
            if (front == null) {
                rear = null;
            }
            size--;
            return parcel;
        }

        public boolean isEmpty() {
            return front == null;
        }

        public int getSize() {
            return size;
        }

        public boolean removeByID(String parcelID) {
            Node current = front;
            Node previous = null;

            while (current != null) {
                if (current.parcel.getParcelID().equals(parcelID)) {
                    if (previous == null) {
                        front = current.next;
                    } else {
                        previous.next = current.next;
                    }

                    if (current == rear) {
                        rear = previous;
                    }

                    size--;
                    return true;
                }
                previous = current;
                current = current.next;
            }
            return false;
        }

        public void printAll() {
            Node current = front;
            while (current != null) {
                System.out.println("  - Parcel ID: " + current.parcel.getParcelID());
                current = current.next;
            }
        }
    }

    private class BSTNode {
        String cityName;
        ParcelQueue parcelQueue;
        BSTNode left;
        BSTNode right;

        BSTNode(String cityName) {
            this.cityName = cityName;
            this.parcelQueue = new ParcelQueue();
            this.left = null;
            this.right = null;
        }
    }

    private BSTNode root;

    public void insertParcel(Parcel parcel) {
        root = insertRecursive(root, parcel);
    }

    private BSTNode insertRecursive(BSTNode node, Parcel parcel) {
        if (node == null) {
            node = new BSTNode(parcel.getDestinationCity());
            node.parcelQueue.enqueue(parcel);
            return node;
        }
        int compare = parcel.getDestinationCity().compareToIgnoreCase(node.cityName);
        if (compare < 0) {
            node.left = insertRecursive(node.left, parcel);
        } else if (compare > 0) {
            node.right = insertRecursive(node.right, parcel);
        } else {
            node.parcelQueue.enqueue(parcel);
        }
        return node;
    }

    public ParcelQueue getCityParcels(String city) {
        BSTNode node = findCityNode(root, city);
        if (node != null) return node.parcelQueue;
        return null;
    }

    private BSTNode findCityNode(BSTNode node, String city) {
        if (node == null) return null;
        int compare = city.compareToIgnoreCase(node.cityName);
        if (compare < 0) return findCityNode(node.left, city);
        else if (compare > 0) return findCityNode(node.right, city);
        else return node;
    }

    public boolean removeParcel(String city, String parcelID) {
        BSTNode node = findCityNode(root, city);
        if (node != null) {
            return node.parcelQueue.removeByID(parcelID);
        }
        return false;
    }

    public int countCityParcels(String city) {
        BSTNode node = findCityNode(root, city);
        if (node != null) {
            return node.parcelQueue.getSize();
        }
        else {
            return 0;}
    }

    public void inOrderTraversal() {
        inOrderRecursive(root);
    }

    private void inOrderRecursive(BSTNode node) {
        if (node == null) return;
        inOrderRecursive(node.left);
        System.out.println("City: " + node.cityName + " | Parcel Count: " + node.parcelQueue.getSize());
        node.parcelQueue.printAll();
        inOrderRecursive(node.right);
    }

    public int getNodeCount() {
        return countNodes(root);
    }

    private int countNodes(BSTNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }


    public int getHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(BSTNode node) {
        if (node == null) {
            return -1;
        }
        int leftHeight = calculateHeight(node.left);
        int rightHeight = calculateHeight(node.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    private class CityCount {
        String city;
        int count;

        CityCount(String city, int count) {
            this.city = city;
            this.count = count;
        }
    }

    private CityCount findCityWithMostParcels(BSTNode node, CityCount max) {
        if (node == null) return max;

        if (node.parcelQueue.getSize() > max.count) {
            max.city = node.cityName;
            max.count = node.parcelQueue.getSize();
        }
        max = findCityWithMostParcels(node.left, max);
        max = findCityWithMostParcels(node.right, max);
        return max;
    }

    public String getCityWithMostParcels() {
        CityCount max = new CityCount(null, -1);
        max = findCityWithMostParcels(root, max);
        return max.city;
    }

    private int countParcelsInQueuesRecursive(BSTNode node) {
        if (node == null) {
            return 0;
        }
        return node.parcelQueue.getSize() +
                countParcelsInQueuesRecursive(node.left) +
                countParcelsInQueuesRecursive(node.right);
    }

    public int getTotalParcelsInAllQueues() {
        return countParcelsInQueuesRecursive(root);
    }

}