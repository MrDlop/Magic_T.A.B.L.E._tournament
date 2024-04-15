package com.mygdx.tablegame.network;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class RPCData {
    public int NetworkComponentID;
    public String MethodName;
    public Object[] Args;
    private String SerializedArgs;
    public Type[] ArgsTypes;

    public static String SerializeRPCData(RPCData RPC)
    {
        Gson gson = new Gson();

        RPCData rpc = new RPCData();
        rpc.NetworkComponentID = RPC.NetworkComponentID;
        rpc.MethodName = RPC.MethodName;
        rpc.ArgsTypes = RPC.ArgsTypes;
        rpc.Args = null;
        rpc.SerializedArgs = "";

        for (int i = 0; i < RPC.Args.length; i++)
        {
            rpc.SerializedArgs += gson.toJson(RPC.Args[i]) + ";";
        }

        String result = gson.toJson(rpc);
        return result;
    }

    public static RPCData DeserializeRPCData(String Data)
    {
        Gson gson = new Gson();
        RPCData raw_rpc = gson.fromJson(Data, RPCData.class.getGenericSuperclass());

        RPCData RPC = new RPCData();
        RPC.NetworkComponentID = raw_rpc.NetworkComponentID;
        RPC.MethodName = raw_rpc.MethodName;
        RPC.ArgsTypes = raw_rpc.ArgsTypes;
        RPC.Args = new Object[RPC.ArgsTypes.length];

        String[] args = raw_rpc.SerializedArgs.split(";");
        for (int i = 0; i < args.length; i++)
        {
            RPC.Args[i] = gson.fromJson(args[i], RPC.ArgsTypes[i]);
        }

        return RPC;
    }
}
