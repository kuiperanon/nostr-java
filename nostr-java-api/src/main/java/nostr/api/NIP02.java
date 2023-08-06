/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nostr.api;

import nostr.api.factory.EventFactory;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nostr.base.PublicKey;
import nostr.event.BaseTag;
import nostr.event.impl.ContactListEvent;

/**
 *
 * @author eric
 */
public class NIP02 extends Nostr {

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class ContactListEventFactory extends EventFactory<ContactListEvent> {

        public ContactListEventFactory(String content) {
            super(content);
        }

        public ContactListEventFactory(List<BaseTag> tags, String content) {
            super(tags, content);
        }

        @Deprecated
        public ContactListEventFactory(PublicKey sender, String content) {
            super(sender, content);
        }

        @Override
        public ContactListEvent create() {
            return new ContactListEvent(getSender(), getTags());
        }
    }

    public static class Kinds {

        public static final Integer KIND_CONTACT_LIST = 3;
    }

}
