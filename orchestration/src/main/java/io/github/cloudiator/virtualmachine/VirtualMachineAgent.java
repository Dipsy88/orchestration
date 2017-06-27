package io.github.cloudiator.virtualmachine;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.cloudiator.messaging.kafka.KafkaMessagingModule;

public class VirtualMachineAgent {

  private static Injector injector = Guice.createInjector(new KafkaMessagingModule());

  /** 
   * starts the virtual machine agent.
   * @param args args
   */
  public static void main(String[] args) {
    final CreateVirtualMachineSubscriber createSubscriber =
        injector.getInstance(CreateVirtualMachineSubscriber.class);
    createSubscriber.run();
  }
}
