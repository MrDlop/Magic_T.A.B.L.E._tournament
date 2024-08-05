package com.mygdx.tablegame.network;

public class NetworkComponent {

    public static NetworkComponent GetNetworkComponent(Object obj)
    {
        NetworkComponentInterface ComponentOwner = (NetworkComponentInterface)obj;
        if (ComponentOwner != null)
        {
            return ComponentOwner.GetNetworkComponent();
        }
        return null;
    }

    public NetworkComponent(Object owner)
    {
        ID = Network.Get().GetNewNetworkComponentID();
        Network.Get().RegisterComponent(this, ID);

        Owner = owner;
    }
    private final int ID;
    public Integer GetID()
    {
        return ID;
    }
    private final Object Owner;
    public Object GetOwner()
    {
        return Owner;
    }
}
