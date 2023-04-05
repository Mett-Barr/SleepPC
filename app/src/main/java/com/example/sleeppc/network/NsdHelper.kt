package com.example.sleeppc.network

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log

class NsdHelper(context: Context) : NsdManager.DiscoveryListener {


    // test
    var callback: (String) -> Unit = {}

    private val TAG = "!!!"
    private val SERVICE_TYPE = "_http._tcp."
    private val mNsdManager: NsdManager =
        context.getSystemService(Context.NSD_SERVICE) as NsdManager

    fun discoverServices(

        // test
        cb: (String) -> Unit = {}

    ) {
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, this)

        // test
        callback = cb
    }

    fun stopDiscovery() {
        mNsdManager.stopServiceDiscovery(this)
    }

    override fun onDiscoveryStarted(regType: String) {
        Log.d(TAG, "Service discovery started")
    }

    override fun onServiceFound(service: NsdServiceInfo) {
        Log.d(TAG, "Service found: ${service.serviceName}")
        mNsdManager.resolveService(service, object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                Log.e(TAG, "Resolve failed: $errorCode")
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
                Log.d(TAG, "Resolve Succeeded. ${serviceInfo?.serviceName}")
                Log.d(TAG, "Service Info: ${serviceInfo?.host?.hostAddress}:${serviceInfo?.port}")


                // new
                // my code
                callback("${serviceInfo?.host?.hostAddress}:${serviceInfo?.port}")
            }
        })
    }

    override fun onServiceLost(service: NsdServiceInfo) {
        Log.e(TAG, "Service lost: ${service.serviceName}")
    }

    override fun onDiscoveryStopped(serviceType: String) {
        Log.i(TAG, "Discovery stopped: $serviceType")
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        Log.e(TAG, "Discovery failed: Error code: $errorCode")
        mNsdManager.stopServiceDiscovery(this)
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        Log.e(TAG, "Discovery failed: Error code: $errorCode")
        mNsdManager.stopServiceDiscovery(this)
    }


//    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
//    private val serviceType = "_http._tcp."
//
//    fun discoverServices() {
//        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, this)
//    }
//
//    override fun onDiscoveryStarted(serviceType: String) {
//        Log.d("NsdHelper", "Service discovery started")
//    }
//
//    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
//        Log.d("NsdHelper", "Service found: $serviceInfo")
//    }
//
//    override fun onServiceLost(serviceInfo: NsdServiceInfo) {
//        Log.d("NsdHelper", "Service lost: $serviceInfo")
//    }
//
//    override fun onDiscoveryStopped(serviceType: String) {
//        Log.d("NsdHelper", "Service discovery stopped")
//    }
//
//    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
//        Log.e("NsdHelper", "Discovery start failed with error code: $errorCode")
//        nsdManager.stopServiceDiscovery(this)
//    }
//
//    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
//        Log.e("NsdHelper", "Discovery stop failed with error code: $errorCode")
//        nsdManager.stopServiceDiscovery(this)
//    }
}
