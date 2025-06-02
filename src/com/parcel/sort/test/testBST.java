package com.parcel.sort.test;
import com.parcel.sort.data_structures.DestinationSorter;
import com.parcel.sort.entities.Parcel;
import com.parcel.sort.entities.Parcel.Size;
import com.parcel.sort.entities.Parcel.Status;


public class testBST {
    public static void main(String[] args) {
        // 1. DestinationSorter objesi oluştur
        DestinationSorter sorter = new DestinationSorter();

        // 2. Paketleri oluştur
        Parcel p1 = new Parcel("P001", "Ankara", 2, Size.Small , 1, Status.InQueue);
        Parcel p2 = new Parcel("P002", "Istanbul", 3, Size.Small , 2, Status.InQueue);
        Parcel p3 = new Parcel("P003", "Izmir", 1, Size.Large , 3, Status.InQueue);
        Parcel p4 = new Parcel("P004", "Istanbul", 2, Size.Small , 4, Status.InQueue);
        Parcel p5 = new Parcel("P005", "Ankara", 1, Size.Medium, 5, Status.InQueue);

        // 3. Paketleri DestinationSorter'a ekle
        sorter.insertParcel(p1);
        sorter.insertParcel(p2);
        sorter.insertParcel(p3);
        sorter.insertParcel(p4);
        sorter.insertParcel(p5);

        // 4. countCityParcels testi
        System.out.println("Parcels in Ankara: " + sorter.countCityParcels("Ankara")); // 2
        System.out.println("Parcels in Istanbul: " + sorter.countCityParcels("Istanbul")); // 2
        System.out.println("Parcels in Izmir: " + sorter.countCityParcels("Izmir")); // 1
        System.out.println("Parcels in Bursa: " + sorter.countCityParcels("Bursa")); // 0

        // 5. getCityWithMostParcels testi
        String busiest = sorter.getCityWithMostParcels();
        System.out.println("Busiest City: " + busiest); // Istanbul or Ankara (2)

        // 6. getNodeCount ve getHeight testi
        System.out.println("Total city nodes: " + sorter.getNodeCount()); // 3
        System.out.println("BST height: " + sorter.getHeight()); // depends on insertion order

        // 7. inOrderTraversal testi
        System.out.println("In-order Traversal:");
        sorter.inOrderTraversal();
    }
}
