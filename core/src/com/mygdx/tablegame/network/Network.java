package com.mygdx.tablegame.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/*
  Основной класс для работы по сети.
  Предполагает использование дочернего класса.
  Является синглтоном. Первый созданный экземпляр будет использоваться в качестве синглтона.
 */
public class Network 
{
    public Network()
    {
        if (Instance == null)
        {
            Instance = this;
        }
        NetworkComponents.put(0,null);
    }

    private static Network Instance = null;
    // Возвращает синглтон
    public static Network Get()
    {
        return Instance;
    }

    protected boolean is_server = false;
    // Возвращает, работает ли Network в режиме сервера
    public boolean IsServer() 
    {
        return is_server;
    }

    // @TODO: Add method for getting this device's ip
    public String GetDeviceIP() throws UnknownHostException
    {
        return Inet4Address.getLocalHost().getHostAddress();
    }


    protected HashMap<Integer, NetworkComponent> NetworkComponents = new HashMap<Integer, NetworkComponent>();
    // Регистрирует NetworkComponent. Не вызывать вручную.
    public void RegisterComponent(NetworkComponent component, int id )
    {
        NetworkComponents.put(id, component);
    }
    /* 
    * Убирает регистрацию NetworkComponent.
    * Необходимо вызывать перед удалением владельца компонента для избегания утечки памяти. 
    */
    public void UnregisterComponent(int ComponentID)
    {
        NetworkComponents.remove(ComponentID);
    }
    // Возвращает свободный ID. Не предназначена для вызова вручную
    public int GetNewNetworkComponentID()
    {
        return Collections.max(NetworkComponents.keySet()) + 1;
    }
    // Возращает компонент, зарегистрированный с данным ID
    public NetworkComponent GetNetworkComponent(int ID)
    {
        return NetworkComponents.get(ID);
    }


    // Определяет, будут ли подниматься исключения, если они возникнут при попытке подключения
    public boolean throw_exception_if_cant_connect = false;
    // Пытается ли Network подключиться в данный момент
    public boolean is_trying_to_connect = false;
    private String IP_to_connect;
    private int port_to_connect;
    // Пытается подключиться к указаному IP
    public void Connect(String IP, int port)
    {
        if (is_trying_to_connect)
        {
            return;
        }
        IP_to_connect = IP;
        port_to_connect = port;
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                try {
                    ConnectInternal(IP_to_connect, port_to_connect);
                } catch (IOException ignored) { }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    // Метод, содержащий логику подключения, запускаемую в отдельном потоке
    protected void ConnectInternal(String IP, int port) throws UnknownHostException, IOException
    {
        is_trying_to_connect = true;
        if (throw_exception_if_cant_connect)
        {
            client_socket = new Socket(IP, port);
            is_trying_to_connect = false;
            Connected();
        }
        else
        {
            try
            {
                client_socket = new Socket(IP, port);
                is_trying_to_connect = false;
                Connected();
            }
            catch (IOException i) {
                System.out.println(i);
                return;
            }
        }
    }
    // Метод для отключения от сессии
    public void Disconnect()
    {
        if (client_socket == null || !client_socket.isConnected()) return;
        try {
            client_socket.close();
        } catch (IOException e) { }
        Disconnected();
    }

    // Создаёт серверный сокет и переводит Network в режим сервера
    public void StartServerSocket(int port) throws IOException
    {
        server_socket = new ServerSocket(port);
        connected_players_socket = new ArrayList<Socket>();
        network_input_streams = new ArrayList<DataInputStream>();
        is_server = true;
    }
    // Ожидает ли Network подключения к серверу
    public boolean is_waiting_connection = false;
    // Запускает ожидание подключения
    public void WaitConnection()
    {
        if (is_waiting_connection) return;
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                try {
                    WaitConnectionInternal();
                } catch (Exception e) { }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    // Логика ожидания подключения, запускаемая в отдельном потоке
    protected void WaitConnectionInternal() throws IOException 
    {
        is_waiting_connection = true;
        Socket connected_socket = server_socket.accept();
        connected_players_socket.add(connected_socket);
        is_waiting_connection = false;
        network_input_streams.add(new DataInputStream(connected_socket.getInputStream()));
        PlayerConnected();
    }

    // Метод, вызываемый когда Network подключается к серверу
    protected void Connected()
    {
        is_server = false;
    }
    // Метод, вызываемый когда к Network подключается клиент
    protected void PlayerConnected(){}
    // Метод, вызываемый когда Network отключается от сервера
    protected void Disconnected() {}


    protected Socket client_socket;
    protected DataInputStream client_input_stream;
    protected ServerSocket server_socket;
    protected ArrayList<Socket> connected_players_socket;
    protected ArrayList<DataInputStream> network_input_streams;

    // Метод, вызывающий RPC
    public void CallRPC(RPCType type, int NetworkComponentID, String MethodName, Object... args) throws IOException
    {
        if ((type == RPCType.Client && !IsServer()) || (type == RPCType.Server && IsServer()))
        {
            CallMethodByName(NetworkComponentID, MethodName, args);
            return;
        }

        RPCData RPC = new RPCData(); 
        RPC.NetworkComponentID = NetworkComponentID;
        RPC.MethodName = MethodName;
        RPC.Args = args;
        RPC.ArgsTypes = new Type[RPC.Args.length];
        for (int i = 0; i < RPC.Args.length; i++)
        {
            RPC.ArgsTypes[i] = RPC.Args[i].getClass().getGenericSuperclass();
        }

        // JSON serialization
        String Data = RPCData.SerializeRPCData(RPC);

        if (type == RPCType.Client)
        {
            for (Socket client : connected_players_socket)
            {
                // Send RPC to client
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                out.writeUTF(Data);
                out.close();
            }
            return;
        }
        if (type == RPCType.Server)
        {
            // Send RPC to server
            DataOutputStream out = new DataOutputStream(client_socket.getOutputStream());
            out.writeUTF(Data);
            out.close();
            return;
        }

    }

    // Метод, проверяющий входные каналы ожидая RPC
    public void Update()
    {
        if (IsServer()) {
        // Check input streams for new rpcs
       for (int i = 0; i < network_input_streams.size(); i++)
       {
            String Data = "";
            try {
                Data = network_input_streams.get(i).readUTF();
            } catch (IOException e) { }

            if (Data != "")
            {
                RecieveRPC(ConstructRPCData(Data));
            }
       }
        }
        else 
        {
            String Data = "";
            try {
                Data = client_input_stream.readUTF();
            } catch (IOException e) { }

            if (Data != "")
            {
                RecieveRPC(ConstructRPCData(Data));
            }
        }
    }

    protected RPCData ConstructRPCData(String Data)
    {
        return RPCData.DeserializeRPCData(Data);
    }

    protected void RecieveRPC(RPCData RPC)
    {
        CallMethodByName(RPC.NetworkComponentID, RPC.MethodName, RPC.Args);
    }
    public void CallMethodByName(int NetworkComponentID, String MethodName, Object[] Args)
    {
        Method method = null;
        Object object = NetworkComponents.get(NetworkComponentID).GetOwner();
        Class<?>[] params = new Class[Args.length];
        for (int i = 0; i < params.length; i++)
        {
            params[i] = Args[i].getClass();
        }
        try {
            method = object.getClass().getDeclaredMethod(MethodName, params);
        } catch (NoSuchMethodException | SecurityException e) { }
        try {
            method.invoke(object, Args);
        } catch (IllegalAccessException | InvocationTargetException e) { }
    }
}
